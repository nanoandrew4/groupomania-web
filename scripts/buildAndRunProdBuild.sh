#!/bin/bash

cd ..
mvn clean package docker:build
cd docker/production/
docker-compose build
docker-compose up
docker-compose down