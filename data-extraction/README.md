Open Library Data Extractor
===

Uses dumps provided by Open Library to create indexes which can be
used to search for:

- authors
- works
- editions

A number of indexes can be created:

- works by title keywords (inverse index)
- works by id

Usage
---

Dumps should be placed in a directory adjacent to this file called
'dumps'. Each of the extraction scripts has usage which can be queried
using the '--help' options. When run, indexes are placed in the
directory 'indexes' adjacent to this file.

```bash
python data-extractions/extract-works.py by-id dumps/<dump file>
```