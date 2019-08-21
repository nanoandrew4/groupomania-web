#!/bin/bash

docker exec -it mongo_mongo-cfg1_1 bash -c "echo 'rs.initiate({_id: \"mongors1conf\",configsvr: true, members: [{ _id : 0, host : \"mongo-cfg1\" },{ _id : 1, host : \"mongo-cfg2\" },{ _id : 2, host : \"mongo-cfg3\" }]})' | mongo --username greenapperadmin --password supersecretpassword" &&\
docker exec -it mongo_mongorsn1_1 bash -c "echo 'rs.initiate({_id : \"mongors1\", members: [{ _id : 0, host : \"mongorsn1\" },{ _id : 1, host : \"mongorsn2\" },{ _id : 2, host : \"mongorsn3\" }]})' | mongo" && \
docker exec -it mongo_mongo-router_1 bash -c "echo 'sh.addShard(\"mongors1/mongorsn1\")' | mongo --username greenapperadmin --password supersecretpassword" && \
docker exec -it mongo_mongorsn1_1 bash -c "echo 'use loggingdb' | mongo" && \
docker exec -it mongo_mongo-router_1 bash -c "echo 'sh.enableSharding(\"loggingdb\")' | mongo --username greenapperadmin --password supersecretpassword" && \
docker exec -it mongo_mongo-router_1 bash -c "echo 'sh.shardCollection(\"loggingdb.logs\", {\"message\" : \"hashed\"})' | mongo --username greenapperadmin --password supersecretpassword"