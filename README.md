# Build

* test if application is running as expected `mvn clean spring-boot:run` ==> http://localhost:8080/actuator/health
* build docker image `mvn spring-boot:build-image`
* test docker image `docker run -p 80:8080 meet-stackit:1` ==> http://localhost/actuator
* tag image as your global one. e.g.: `docker tag meet-stackit:1 hoehne/meet-stackit`
* push image to remote repo. e.g.: `docker push hoehne/meet-stackit`









