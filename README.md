# Setup

* Create K8N Cluster on https://portal.stackit.cloud
* Download kubeconfig an store it as .kube/config
* check if it you are able to connect `kubectl get all`

# Build

* test if application is running as expected `mvn clean spring-boot:run` ==> http://localhost:8080/actuator/health
* build docker image `mvn spring-boot:build-image`
* test docker image `docker run -p 80:8080 meet-stackit:1` ==> http://localhost/actuator








