from collections import defaultdict
from queue import Empty
from rejson import Client, Path
import json
import jsonstreams
import traceback


class IxEncoder(json.JSONEncoder):
    def default(self, o):
        if isinstance(o, set):
            return list(o)
        return super(IxEncoder, self).default(o)


def gatherer_by_keyword_fs(response_q, error_q):
    ix = defaultdict(set)
    try:
        while True:
            works_id, title_words = response_q.get(block=True, timeout=5)
            for w in title_words:
                ix[w].add(works_id)
    except Empty:
        print('Ended gathering worker')
    except Exception as e:
        trace = traceback.format_exc()
        error_q.put('{}{}'.format('gatherer->fs->by-keyword > ', trace), block=True)

    with open('works-by-id.ix', 'wb+') as f:
        f.write(json.dumps(ix, cls=IxEncoder).encode())

def gatherer_by_keyword_redis(response_q, error_q):
    r = Client(host='localhost',
               port=6379,
               decode_responses=True)
    try:
        while True:
            try:
                works_id, title_words = response_q.get(block=True, timeout=5)
                pipeline = r.pipeline(True)
                for w in title_words:
                    pipeline.sadd('keyword:{}'.format(w), works_id)
                pipeline.execute()
            except Empty:
                raise Empty
            except Exception as e:
                trace = traceback.format_exc()
                error_q.put('{}{}'.format('gatherer->redis->by-keyword > ', trace), block=True)
                pass
    except Empty:
        print('Ended gathering worker')

        
def gatherer_by_id_fs(response_q, error_q):
    try:
        with jsonstreams.Stream(jsonstreams.Type.object, filename='works-by-id.ix') as s:
            while True:
                works_id, extracted_data = response_q.get(block=True, timeout=5)
                s.write(works_id, extracted_data)
    except Empty:
        print('Ended gathering worker')
    except Exception as e:
        trace = traceback.format_exc()
        error_q.put('{}{}'.format('gatherer->fs->by-id > ', trace), block=True)

def gatherer_by_id_redis(response_q, error_q):
    r = Client(host='localhost',
               port=6379,
               decode_responses=True)
    try:
        count = 0
        while True:
            try:
                works_id, extracted_data = response_q.get(block=True, timeout=5)
                count += 1
                if count % 5000 == 0:
                    print(count)
                r.jsonset(works_id, Path.rootPath(), extracted_data)
            except Empty:
                raise Empty
            except Exception as e:
                trace = traceback.format_exc()
                error_q.put('{}{}'.format('gatherer->redis->by-id > ', trace), block=True)
                pass
    except Empty:
        print('Ended gathering worker')
    
