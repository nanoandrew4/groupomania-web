#!/bin/bash

cd ../docker/dev/
docker-compose build
docker-compose up -d

while true; do
    isDbReady=$(pg_isready -h 127.0.0.1 -d groupomania -U greenapperdba)
    if [[ ${isDbReady} == *accepting* ]]; then
        break;
    else
        sleep 1
    fi
done

docker exec -it dev_mongocfg1_1 bash -c "echo 'rs.initiate({_id: \"mongors1conf\",configsvr: true, members: [{ _id : 0, host : \"mongocfg1\" },{ _id : 1, host : \"mongocfg2\" }]})' | mongo --username greenapperadmin --password supersecretpassword"
docker exec -it dev_mongorsn1_1 bash -c "echo 'rs.initiate({_id : \"mongors1\", members: [{ _id : 0, host : \"mongorsn1\" },{ _id : 1, host : \"mongors1n2\" },{ _id : 2, host : \"mongors1n3\" }]})' | mongo"
docker exec -it dev_mongos1_1 bash -c "echo 'sh.addShard(\"mongors1/mongorsn1\")' | mongo --username greenapperadmin --password supersecretpassword"
docker exec -it dev_mongorsn1_1 bash -c "echo 'use loggingdb' | mongo"
docker exec -it dev_mongos1_1 bash -c "echo 'sh.enableSharding(\"loggingdb\")' | mongo loggingdb --username greenapperadmin --password supersecretpassword"
docker exec -it dev_mongorsn1_1 bash -c "echo 'db.createCollection(\"loggingdb.logs\")' | mongo loggingdb"
docker exec -it dev_mongos1_1 bash -c "echo 'sh.shardCollection(\"loggingdb.logs\", {\"message\" : \"hashed\"})' | mongo --username greenapperadmin --password supersecretpassword"

cd ../../
mvn clean spring-boot:run
cd docker/dev/
#docker-compose down