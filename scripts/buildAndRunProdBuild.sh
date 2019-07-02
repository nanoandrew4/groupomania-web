#!/bin/bash

cd ..
mvn clean package docker:build
cd docker/
docker-compose build
docker-compose up
docker-compose down
cd ../scripts/
