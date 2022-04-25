# Stuff to read

* https://spring.io/guides/gs/spring-boot-kubernetes/
* https://spring.io/guides/topicals/spring-on-kubernetes/
* https://medium.com/kubernetes-tutorials/monitoring-your-kubernetes-deployments-with-prometheus-5665eda54045

# Setup

* Create K8N Cluster on https://portal.stackit.cloud
* Download kubeconfig an store it as .kube/config
* check if it you are able to connect `kubectl get all`


# Build

* test if application is running as expected `mvn clean spring-boot:run` ==> http://localhost:8080/actuator/health
* build docker image `mvn spring-boot:build-image`
* test docker image `docker run -p 80:8080 meet-stackit:1` ==> http://localhost/actuator
* tag image as your global one. e.g.: `docker tag meet-stackit:1 hoehne/meet-stackit`
* push image to remote repo. e.g.: `docker push hoehne/meet-stackit`


# Deploy to K8N

* apply configuration `kubectl apply -f deployment.yaml`
* Check result `kubectl get all`
* Just redeploy `kubectl -n default rollout restart deployment meet-stackit-pod`


# local test

* activate port forward `kubectl port-forward service/meet-stackit-service 8080:8080` ==> http://localhost:8080/actuator/health


# public test

* get external IP `kubectl get service meet-stackit-service ` e.g.: 193.148.167.139
* open http://193.148.167.139:8080/hello?name=Geht Dich nichts an


# Install K8N Dashboard to get an better visualisation

* `kubectl apply -f https://raw.githubusercontent.com/kubernetes/dashboard/v2.5.0/aio/deploy/recommended.yaml`
* start proxy `kubectl proxy`
* open http://localhost:8001/api/v1/namespaces/kubernetes-dashboard/services/https:kubernetes-dashboard:/proxy/
* select kube config


# Install Prometheus Support 

* Deploy RBAC for access rights `kubectl create -f installPrometheusSupport/deployRBAC.yaml`
* Delete old Prometheus config if available `kubectl delete configmap prometheus-config `
* Deploy Prometheus configuration `kubectl create configmap prometheus-config --from-file ./installPrometheusSupport/prometheus.yaml`
* Deploy Prometheus `kubectl apply -f ./installPrometheusSupport/deployPrometheus.yaml`
* Get IP of Prometheus: `kubectl get service prometheus-service` e.g.: 193.148.173.184
* Check if UI is available http://193.148.173.184:9090







