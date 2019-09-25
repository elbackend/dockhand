# Repo
The repository requires the following configuration for use with Jenkins and Dockhand

### Repositories
There are 2 main types of projects that are building built, Libraries and Applications.

1. Libraries are built and pushed to Nexus. Libraries have 2 Jenkins Jobs: build an deploy. Build will build and test the artifact but not push it in to Nexus (as everything is a snapshot version that replaces the old version). Deployment builds, tests and pushes the compiled Library in to Nexus.

2. Applications are built, promoted and deployed from Jenkins. A build creates, tags and pushes a Docker image if the branch is configured to do so. Once an image is built it can be deployed in to the dev environment. The dev image can also be promoted. This will retag the image with release candidate version which can be deployed in to the stage environment.

## Repository Setup
The repository must be set up to include certain files used by Dockhand for building, promoting and deploying.

### Files
For the repository setup, it is best to start from an existing project, copy over the relevant files and modify them.

#### Frontend Applications (login and reporting)
Note that frontend applications are rebuilt when they are promoted since environment properties are injected at build time.

```
# Common configurations for this repo's Jenkins pipeline
jenkins.yaml

# Jenkins files for build, promote and deploy pipelines
Jenkinsfile
Jenkinsfile.promote
Jenkinsfile.deploy

# Dockerfile for the application
Dockerfile

# The docker-compose used to build, tag and push the application
cicd/docker/docker-compose.yaml

# The docker-compose file for building the "promoted" image 
cicd/promote/stage/docker-compose.yaml

# Helm chart for deploying to Kubernetes
cicd/deploy/<helm-chart>
cicd/deploy/<helm-chart>/values-dev.yaml
cicd/deploy/<helm-chart>/values-stage.yaml
```

#### Applications
```
# Common configurations for this repo's Jenkins pipeline
jenkins.yaml

# Jenkins files for build, promote and deploy pipelines
Jenkinsfile
Jenkinsfile.promote
Jenkinsfile.deploy

# Dockerfile for the application
Dockerfile

# The docker-compose used to build, tag and push the application
cicd/docker/docker-compose.yaml

# Helm chart for deploying to Kubernetes
cicd/deploy/<helm-chart>
cicd/deploy/<helm-chart>/values-dev.yaml
cicd/deploy/<helm-chart>/values-stage.yaml
```

#### Libraries
```
# Jenkins files for build, promote and deploy pipelines
Jenkinsfile
Jenkinsfile.deploy

# Dockerfile for the application
Dockerfile
# Dockerfile that pushes library to nexus
Dockerfile.deploy

# build script for pipelines (builds Dockerfile)
cicd/build/build.sh
# deploy script for pipelines (builds Dockerfile.deploy)
cicd/deploy/deploy.sh
```
