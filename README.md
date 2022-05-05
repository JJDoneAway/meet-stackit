# meet-stackit demo application
This repo describes a very manual setup wit no CD integration at all. Just to learn the basic details


# Stuff to read

* https://spring.io/guides/gs/spring-boot-kubernetes/
* https://spring.io/guides/topicals/spring-on-kubernetes/
* https://medium.com/kubernetes-tutorials/monitoring-your-kubernetes-deployments-with-prometheus-5665eda54045
* https://github.com/prometheus-operator/prometheus-operator/blob/main/Documentation/user-guides/getting-started.md ==> Prometheus Operator


# Setup

* Create K8N Cluster on https://portal.stackit.cloud
* Download kubeconfig an store it as .kube/config
* Install Ingress Controller https://kubernetes.github.io/ingress-nginx/deploy/ (`kubectl deploy -f kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/controller-v1.2.0/deploy/static/provider/cloud/deploy.yaml`)
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
* start an interactive linux container in the cluster for investigations `kubectl run -it --rm  --image alpine/curl johannes /bin/sh`


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








