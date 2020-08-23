from collections import defaultdict
from queue import Empty
import json
import jsonstreams

class IxEncoder(json.JSONEncoder):
    def default(self, o):
        if isinstance(o, set):
            return list(o)
        return super(IxEncoder, self).default(o)


def gatherer_by_keyword(response_q):
    ix = defaultdict(set)
    try:
        while True:
            works_id, title_words = response_q.get(block=True, timeout=5)
            for w in title_words:
                ix[w].add(works_id)
    except Empty:
        print('Ended gathering worker')
    except Exception as e:
        print('gathere: {}'.format(e))

    with open('works-by-id.ix', 'wb+') as f:
        f.write(json.dumps(ix, cls=IxEncoder).encode())

def gatherer_by_id(response_q):
    try:
        with jsonstreams.Stream(jsonstreams.Type.object, filename='works-by-id.ix') as s:
            while True:
                works_id, extracted_data = response_q.get(block=True, timeout=5)
                s.write(works_id, extracted_data)
    except Empty:
        print('Ended gathering worker')
    except Exception as e:
        print('gatherer: {}'.format(e))

    # with open('works-by-id.ix', 'wb+') as f:
    #     f.write(json.dumps(ix, cls=IxEncoder).encode())
