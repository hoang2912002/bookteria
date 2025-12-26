# Notification service

## Prerequisites

### Mongodb
Install Mongodb from Docker Hub

`docker pull mongodb/mongodb-community-server:7.0-ubi8`

Start Mongodb server at port 27017 with root username and password: root/root

`docker run --name mongodb-7.0 -d  -p 27017:27017 -e MONGO_INITDB_ROOT_USERNAME=root -e MONGO_INITDB_ROOT_PASSWORD=root mongodb/mongodb-community-server:7.0-ubi8`