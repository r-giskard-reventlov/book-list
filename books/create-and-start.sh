#!/bin/bash

docker run -p 6379:6379 --name books -v $PWD/data:/data redislabs/rejson:latest --appendonly yes --loadmodule /usr/lib/redis/modules/rejson.so
