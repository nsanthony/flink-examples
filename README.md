# flink-examples
set of example flink code and deployment files

To start the flink cluster:
```bash
cd k8s/custom/
./start.sh
k create -f wordcount.yaml
kubectl port-forward <jobmanager-pod-name> 8081:8081
```

To see the flink UI go to `http://localhost:8081/`. 

To stop the flink cluster (from the k8s/ directory)
```bash
./stop.sh
```


To start a flink operator:
```bash
cd k8s/lyft/
./start.sh
minikube service wordcount-operator-example -n flink-operator
```


To compile and submit the job:
```bash
./gradlew clean build
```

Upload the flink-0.1.0-all.jar to the flink UI. 

The job can take 2 parameters. The first is the number of events to generate in order from 0 to the number specified. The second is the 
max number you would like to save in state (i.e. it will only save numbers 0 -> 10 in state and their strings if you set it to 10). Both of 
these parameters have defaults so don't worry if you forget. 
