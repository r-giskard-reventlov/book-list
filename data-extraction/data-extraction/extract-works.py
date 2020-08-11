"""Usage:
    extract-works.py by-keyword FILE
    extract-works.py by-id FILE

Process a works file from Open library, creating indexed works

Arguments:
  by-keyword  Creates an index of works id's indexed by title keywords
  by-id       Creates an index of works objects indexed by works id
  FILE        Name of Works file to process

Options:
  -h --help
  -v       verbose mode
  -q       quiet mode
"""
from docopt import docopt

from multiprocessing import Process, Queue, Manager
from gatherers import gatherer_by_id, gatherer_by_keyword
from workers import worker_by_id, worker_by_keyword


def dispatcher(job_q, works_file):
    with open(works_file, 'r') as f:
        works_record = f.readline()
        while works_record:
            job_q.put(works_record)
            works_record = f.readline()
    print('Ended dispatcher')


if __name__ == '__main__':
    arguments = docopt(__doc__)
    
    job_q = Queue()
    response_q = Queue()
    
    processes = []

    print(arguments)
    
    works_file = arguments['FILE']
    gatherer = gatherer_by_keyword if arguments['by-keyword'] else gatherer_by_id
    worker = worker_by_keyword if arguments['by-keyword'] else worker_by_id
    
    process = Process(target=dispatcher, args=(job_q, works_file))
    processes.append(process)
    process.start()

    process = Process(target=gatherer, args=(response_q,))
    processes.append(process)
    process.start()

    
    for ix, i in enumerate(range(1, 20)):
        process = Process(target=worker, args=(job_q, response_q, ix))
        processes.append(process)
        process.start()

    for proc in processes:
        proc.join()

