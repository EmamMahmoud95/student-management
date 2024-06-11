#!/bin/bash
docker-compose -f student_management-docker-compose.yaml down -v

docker build -t studentmanagement -f DockerFile .

## this command is useful for windows users, you can comment this command if you're a mac/linux user
#sed -i -e 's/\r$//' init-app-db.sh

docker-compose -f student_management-docker-compose.yaml up --build