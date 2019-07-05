#!/bin/bash

cd ../docker/dev/
docker-compose build
docker-compose up -d

while true; do
    isDbReady=$(pg_isready -h 127.0.0.1 -d groupomania -U greenapperdba)
    if [[ $isDbReady == *accepting* ]]; then
        break;
    else
        sleep 1
    fi
done

cd ../../
mvn clean spring-boot:run
cd docker/dev/
docker-compose down