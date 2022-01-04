# flink-examples
set of example flink code and deployment files

To start the flink cluster:
```bash
cd k8s
./start.sh
kubectl port-forward <jobmanager-pod-name> 8081:8081
```

To stop the flink cluster (from the k8s/ directory)
```bash
./stop.sh
```


