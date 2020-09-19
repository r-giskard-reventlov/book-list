import re
import json
import sys
import os
from rejson import Client, Path



def gatherer_authors_redis():
    r = Client(host='localhost',
               port=6379,
               decode_responses=True)

    with open('./../dumps/ol_dump_authors_2020-06-30.txt', 'r') as f:
        author_record = f.readline()
        while author_record:
            try:
                author = {}
                author_split = re.split(r'\t+', author_record)
                author_id = author_split[1]
                author_json = json.loads(author_split[4])
                if author_json:
                    if 'name' in author_json:
                        author['name'] = author_json['name']
                    if 'title' in author_json:
                        author['title'] = author_json['title']
                    if 'birth_date' in author_json:
                        author['dob'] = author_json['birth_date']
                    if 'death_date' in author_json:
                        author['dod'] = author_json['death_date']
                    if 'bio' in author_json and 'value' in author_json['bio']:
                        author['bio'] =  author_json['bio']['value']

                r.jsonset(author_id, Path.rootPath(), author)
            except Exception as e:
                print('error {}'.format(e))

            author_record = f.readline()

gatherer_authors_redis()
