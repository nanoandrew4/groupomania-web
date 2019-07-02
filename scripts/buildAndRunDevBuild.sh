#!/bin/bash

cd ..
mvn -Dmaven.test.skip=true clean package docker:build
cd docker/
docker-compose build
docker-compose up
docker-compose down
cd ../scripts/