#!/bin/bash

kubectl create -f crd.yaml 
kubectl create -f namespace.yaml 
kubectl create -f role.yaml 
kubectl create -f role-binding.yaml 
kubectl create -f config.yaml 
kubectl create -f flinkk8soperator.yaml
