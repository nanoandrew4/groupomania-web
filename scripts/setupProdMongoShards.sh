#!/bin/bash

echo "Starting MongoDB sharded cluster setup script..."
echo "Setting up configuration server replica set..."
docker exec -it mongo_mongocfg1_1 bash -c "echo 'rs.initiate({_id: \"mongors1conf\",configsvr: true, members: [{ _id : 0, host : \"mongocfg1\" },{ _id : 1, host : \"mongocfg2\" },{ _id : 2, host : \"mongocfg3\" }]})' | mongo --username greenapperadmin --password supersecretpassword" &&\

echo "Setting up replica set 1 with two nodes..."
docker exec -it mongo_mongorsn1_1 bash -c "echo 'rs.initiate({_id : \"mongors1\", members: [{ _id : 0, host : \"mongorsn1\" },{ _id : 1, host : \"mongorsn2\" }]})' | mongo" && \

echo "Setting up replica set 2 with two nodes..."
docker exec -it mongo_mongorsn3_1 bash -c "echo 'rs.initiate({_id : \"mongors2\", members: [{ _id : 0, host : \"mongorsn3\" },{ _id : 1, host : \"mongorsn4\" }]})' | mongo" && \

echo "Waiting for changes to take effect before continuing configuration..."
sleep 15

echo "Adding replica set 1 as shard to cluster..."
docker exec -it mongo_mongorouter_1 bash -c "echo 'sh.addShard(\"mongors1/mongorsn1\")' | mongo --username greenapperadmin --password supersecretpassword" && \

echo "Adding replica set 2 as shard to cluster..."
docker exec -it mongo_mongorouter_1 bash -c "echo 'sh.addShard(\"mongors2/mongorsn3\")' | mongo --username greenapperadmin --password supersecretpassword" && \

echo "Selecting database to use on all replica sets..."
docker exec -it mongo_mongorsn1_1 bash -c "echo 'use loggingdb' | mongo" && \
docker exec -it mongo_mongorsn3_1 bash -c "echo 'use loggingdb' | mongo" && \

echo "Enabling sharding on database and collection used by backend...."
docker exec -it mongo_mongorouter_1 bash -c "echo 'sh.enableSharding(\"loggingdb\")' | mongo --username greenapperadmin --password supersecretpassword" && \
docker exec -it mongo_mongorouter_1 bash -c "echo 'sh.shardCollection(\"loggingdb.logs\", {\"message\" : \"hashed\"})' | mongo --username greenapperadmin --password supersecretpassword"