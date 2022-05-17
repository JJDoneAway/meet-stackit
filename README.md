# Develop
* use DEV profile for local development
* run `docker-compose up` to start Maria DB (same as MySQL)

# Build

* test if application is running as expected `mvn clean spring-boot:run` ==> http://localhost:8080/actuator/health
* build docker image `mvn spring-boot:build-image`
* test docker image `docker run -p 80:8080 meet-stackit:1` ==> http://localhost/actuator
* tag image as your global one. e.g.: `docker tag meet-stackit:1 hoehne/meet-stackit`
* push image to remote repo. e.g.: `docker push hoehne/meet-stackit`

#Help
* start an interactive linux container in the cluster for investigations `kubectl -n meet-stackit run -it --rm  --image alpine/curl johannes /bin/sh`
* start an interactive linux container in the cluster for mysql client `kubectl run -it --rm  --image imega/mysql-client mysql /bin/sh`
* insert a batch of persons `for ((i=1; i<=500; i++)); do curl -X POST "http://localhost:8080/person?name=Johannes_$i"; done` 










