#!/bin/bash

kubectl delete -f flinkk8soperator.yaml
kubectl delete -f config.yaml 
kubectl delete -f role-binding.yaml
kubectl delete -f role.yaml 
kubectl delete -f namespace.yaml 
kubectl delete -f crd.yaml 
