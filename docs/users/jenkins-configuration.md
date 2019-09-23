# Jenkins Setup
## Dockhand
Dockhand is an extensible Jenkins shared library created by BoxBoat. The README at the root of this project explains its purpose and usage. Some items have been added specifically for ExploreLearning's fork of Dockhand. These items extend the base shared library where needed and can be found in [src/com/el/jenkins/pipeline](../../src/com/el/jenkins/pipeline).

### ElCommon.groovy
Invokes Dockhand's base common logic. Extensions to this logic would be added here.

### ElBuild.groovy
Invokes Dockhand's base build logic. Extensions to this logic would be added here.

### ElDeploy.groovy
Invokes Dockhand's base deploy logic. Extended to create and configure the HelmDeploy object.

### ElPromote.groovy
Invokes Dockhand's base promotion logic. Extensions to this logic would be added here.

## Credentials
Credentials are used to store secrets in Jenkins. These secrets may be used for tasks like connecting to GitHub to clone the repository or for authentication with Rancher or Nexus. The credentials used by Dockhand are configurable, and are referenced in the [config.yaml](../../resources/com/boxboat/jenkins/config.yaml)

### Adding a Credentials
From the Jenkins' root, navigate to Credentials > System > Global Credentials and click "Add Credentials" or navigate to `<jenkins_url>/credentials/store/system/domain/_/newCredentials`. Select the kind of credential you wish to add and fill out the relevant fields. Click "OK" when finished.

## Repositories
Each repository in Jenkins contains 2 or 3 pipelines depending on if it is a library or an application. These pipelines should be labeled "<application_name>-build","<application_name>-promote", and "<application_name>-deploy" and nested under a folder called "<application_name>". The "<application_name>-promote" pipeline will be omitted if this is a library project.

For organizational purposes, each application folder is nested under a main folder, either Common or Reflex, depending on the application's purpose.

### Adding a Repository
1. Navigate to either the "Common" or "Reflex" folder
1. Click the "New Item" button
  1. Enter the "<application_name>" as the item name, select "Folder" and click "OK"
1. Navigate to the newly created folder and click the "New Item" button
  1. Enter "<application_name>-build" scroll to the bottom. In "Copy from" enter "../<other-application-name>/<other-application-name>-build" and click "Ok"
  1. Update the "Repository HTTPS URL" field and click "Save"
  1. Repeat this for "<application_name>-promote" and "<application_name>-deploy"

Once saved this will create a WebHook on the repository in GitHub to trigger the Jenkins jobs on commits.
