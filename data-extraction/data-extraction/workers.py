from spacy.lang.en import English
from queue import Empty
import json
import itertools
import re

nlp = English()

def forms(token):
    return [token.lemma_.lower()]

def useful_token(token):
    return not token.is_stop and token.lemma_ and not token.is_punct

def worker_by_keyword(job_q, response_q, ix):
    try:
        while True:
            works_record = job_q.get(block=True, timeout=5)
            works_id, works_json = process_works(works_record)
            if works_json:
                title = nlp(works_json['title'])
                stopless_title = list(itertools.chain.from_iterable(
                    [forms(token) for token in title if useful_token(token)]))
                response_q.put((works_id, stopless_title))
    except Empty:
        print('Ended job worker [{}]'.format(ix))
    except Exception as e:
        print('worker [{}]: {}'.format(ix, e))

def worker_by_id(job_q, response_q, ix):
    try:
        while True:
            works_record = job_q.get(block=True, timeout=5)
            works_id, works_json = process_works(works_record)
            if works_json:
                response_q.put((works_id, works_json))
    except Empty:
        print('Ended job worker [{}]'.format(ix))
    except Exception as e:
        print('worker [{}]: {}'.format(ix, e))

def process_works(works_record):
    works_split = re.split(r'\t+', works_record)
    works_id = works_split[1]
    works_json = json.loads(works_split[4])
    if 'title' in works_json:
        return works_id, works_json
    return None
