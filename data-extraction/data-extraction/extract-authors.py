import re
import json
import sys
import os
from pathlib import Path

with open('./authors', 'w+') as auth:
    with open('./ol_dump_authors_2020-06-30.txt', 'r') as f:
        author_record = f.readline()
        while author_record:
            author_split = re.split(r'\t+', author_record)
            author_id = author_split[1]
            author_json = json.loads(author_split[4])
            if 'name' in author_json:
                author_name = author_json['name']
                if author_name:
                    auth.write('{}\t{}{}'.format(author_id, author_name, os.linesep))
            author_record = f.readline()
        
