mvn spring-boot:build-image
docker tag meet-stackit:1 hoehne/meet-stackit:$1
docker push hoehne/meet-stackit:$1

