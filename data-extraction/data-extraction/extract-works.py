"""Usage:
    extract-works.py by-keyword FILE (fs|redis) [ERROR_FILE]
    extract-works.py by-id FILE (file|redis) [ERROR_FILE]

Process a works file from Open library, creating indexed works

Arguments:
  by-keyword  Creates an index of works id's indexed by title keywords
  by-id       Creates an index of works objects indexed by works id
  FILE        Name of Works file to process
  OUTPUT_FILE Name/path of the output file
  REDIS       Name of the Redis instance for output
  ERROR_FILE  Name of error file to log errors

Options:
  -h --help
  -v       verbose mode
  -q       quiet mode
"""
from docopt import docopt

from multiprocessing import Process, Queue, Manager
from gatherers import gatherer_by_id_fs, gatherer_by_keyword_fs, gatherer_by_id_redis, gatherer_by_keyword_redis
from workers import worker_by_id, worker_by_keyword

def dispatcher(job_q, works_file):
    with open(works_file, 'r') as f:
        works_record = f.readline()
        while works_record:
            job_q.put(works_record, block=True)
            works_record = f.readline()
    print('Ended dispatcher')


def error_processor(error_q, error_file):
    with open(error_file, 'w+') as f:
        while True:
            error = error_q.get(block=True)
            if not error:
                break
            f.write('{}\n'.format(error))
            f.flush()


if __name__ == '__main__':
    arguments = docopt(__doc__)
    
    job_q = Queue(maxsize=500)
    response_q = Queue(maxsize=500)
    error_q = Queue(maxsize=100)
    
    processes = []

    error_file = arguments['ERROR_FILE'] or './log.err'
    works_file = arguments['FILE']
    out = 'file' if arguments['fs'] else 'redis'
    if out == 'file':
        gatherer = gatherer_by_keyword_fs if arguments['by-keyword'] else gatherer_by_id_fs
    else:
        gatherer = gatherer_by_keyword_redis if arguments['by-keyword'] else gatherer_by_id_redis        
    worker = worker_by_keyword if arguments['by-keyword'] else worker_by_id
    
    process = Process(target=dispatcher, args=(job_q, works_file))
    processes.append(process)
    process.start()

    process = Process(target=gatherer, args=(response_q, error_q))
    processes.append(process)
    process.start()

    error_process = Process(target=error_processor, args=(error_q, error_file))
    error_process.start()

    
    for ix, i in enumerate(range(1, 20)):
        process = Process(target=worker, args=(job_q, response_q, error_q, ix))
        processes.append(process)
        process.start()

    for proc in processes:
        proc.join()

    error_q.put(None) # simple way to trigger end of this process

