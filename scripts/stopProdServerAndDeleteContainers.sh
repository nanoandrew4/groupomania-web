#!/bin/bash

cd ../docker/prod
docker-compose down
cd mongo
docker-compose down
sudo rm -rf /mongo_cluster/