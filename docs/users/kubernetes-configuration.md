# Kubernetes
The configurations below only need to be run once per cluster (or potentially per namespace if namespaces are used). Configuring Helm and the registry secret will allow Kubernetes to pull images from the Nexus repository and process Helm charts for deployments.

## Helm
The deployment process uses Helm. Helm version 2 requires a server component to be installed on Kubernetes. Helm version `2.14.3` was installed on both the dev and the stage clusters using the following command

```
helm init --service-account tiller
```

This requires a valid Kubernetes config and the specified version of helm which can be found at [helm.sh](https://helm.sh).

## Registry Credentials
In order to pull Docker images from a private repository, registry credentials must be provided. This is done by creating a secret in Kubernetes with the registry authentication. This secret is referenced in the Helm chart so Kubernetes knows how to pull the image. The registry secret to connect to nexus was created on both the dev and stage clusters using the following command:

```
kubectl create secret docker-registry regcred --docker-server=<your-registry-server> --docker-username=<your-name> --docker-password=<your-pword> --docker-email=<your-email>
```
