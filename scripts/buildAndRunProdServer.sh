#!/bin/bash

./setupDockerSecurity.sh

cd ..
mvn clean package docker:build -Denvironment=production -Dmaven.test.skip=true
cd docker/prod/
docker-compose build
docker-compose up -d

echo "Waiting for server to be live before importing data..."

while true; do
    state=$(docker logs prod_groupomania_1 | awk '$0 ~ /Started Application/ { print "true" }')
    if [[ ${state} == "true" ]]; then
        break
    fi
    sleep 2s
    echo "Still waiting..."
done

cd ../../scripts/
./importSampleData.sh

echo "Data imported successfully"
echo "Server is now ready to accept request"