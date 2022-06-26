#!/bin/bash +x

#docker-compose down --remove-orphans
docker-compose up --build -d
docker images | grep '<none>' | xargs docker rmi

exit $?
