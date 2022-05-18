mvn spring-boot:build-image
docker tag meet-stackit:1 johanneshoehne1498/meet-stackit:$1
docker push johanneshoehne1498/meet-stackit:$1

