from spacy.lang.en import English
from queue import Empty
import json
import itertools
import re
import traceback

nlp = English()

def forms(token):
    return [token.lemma_.lower()]

def useful_token(token):
    return not token.is_stop and token.lemma_ and not token.is_punct

def worker_by_keyword(job_q, response_q, error_q, ix):
    try:
        while True:
            try:
                works_record = job_q.get(block=True, timeout=5)
                works_id, works_json = process_works(works_record)
                if works_json:
                    title = nlp(works_json['title'])
                    stopless_title = list(itertools.chain.from_iterable(
                        [forms(token) for token in title if useful_token(token)]))
                    response_q.put((works_id, stopless_title), block=True)
            except Empty:
                raise Empty('nothing left to process')
            except Exception as e:
                trace = traceback.format_exc()
                error_q.put('{}{}'.format('worker-by-keyword > ', trace), block=True)
                pass
    except Empty:
        print('Ended job worker [{}]'.format(ix))
    except Exception as e:
        print('worker [{}]: {}'.format(ix, e))

def worker_by_id(job_q, response_q, error_q, ix):
    try:
        while True:
            try:
                works_record = job_q.get(block=True, timeout=5)
                works_id, works_json = process_works(works_record)
                if works_json:
                    extracted = {}
                    if 'title' in works_json:
                        extracted['title'] = works_json['title']
                    if 'covers' in works_json and len(works_json['covers']) > 0:
                        extracted['covers'] = works_json['covers']
                    if 'authors' in works_json and len(works_json['authors']) > 0:
                        extracted['authors'] = [a['author']['key'] for a in works_json['authors'] if 'author' in a]
                    if 'description' in works_json and isinstance(works_json['description'], str):
                        extracted['description'] = works_json['description']
                    if 'description' in works_json and 'value' in works_json['description']: 
                        extracted['description'] = works_json['description']['value']
                    if 'subjects' in works_json and len(works_json['subjects']) > 0:
                        extracted['subjects'] = works_json['subjects']
                    if 'first_publish_date' in works_json and len(works_json['first_publish_date']) > 0:
                        extracted['published'] = works_json['first_publish_date']
                    response_q.put((works_id, extracted), block=True)
            except Empty:
                raise Empty('nothing left to process')
            except Exception as e:
                trace = traceback.format_exc()
                error_q.put('{}{}'.format('worker-by-id > ', trace), block=True)
                pass
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
    return None, None
