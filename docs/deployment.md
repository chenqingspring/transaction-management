# Deployment Guide

## Prerequisites

- Java 21
- Maven
- Docker
- Kubernetes cluster

## Building the Application

1. Clone the repository:
   ```bash
   cd transaction-management
   ```

2. Build the application:
   ```bash
   mvn clean package
   ```

3. Build the Docker image:
   ```bash
   docker build -t transaction-management .
   ```

## Deploying to Kubernetes

1. Ensure your Kubernetes cluster is running and `kubectl` is configured.

2. Deploy the application:
   ```bash
   kubectl apply -f kubernetes/deployment.yaml
   ```

3. Verify the deployment:
   ```bash
   kubectl get pods
   kubectl get services
   ```

4. Access the application:
   - The application is exposed via a LoadBalancer service.
   - Use the external IP of the service to access the API.

## Scaling

To scale the application, update the `replicas` field in `kubernetes/deployment.yaml` and reapply:

```bash
kubectl apply -f kubernetes/deployment.yaml
``` 