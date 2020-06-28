package com.el.jenkins.pipeline.deploy

import com.boxboat.jenkins.library.config.Config
import com.boxboat.jenkins.pipeline.deploy.BoxDeploy
import com.boxboat.jenkins.pipeline.deploy.kubernetes.HelmDeploy

class ElDeploy extends BoxDeploy {

    String app;
    String chart_dir;

    ElDeploy(Map config = [:]) {
        super(config)
        setPropertiesFromMap(config)
    }

    public deploy() {
      this.writeImageTags(
        outFile: "${chart_dir}/${app}/image-tags.yaml",
      )

      def releaseName = "${app}-${this.config.deploymentKey}"
      this.allEnvironments().each { env ->
            def helmDeployApp = new HelmDeploy(
                chart: ".",
                directory: "${chart_dir}/${app}",
                name: releaseName,
                options: [
                    "namespace": "default",
                    "set"      : [],
                    "values"   : [
                        "image-tags.yaml",
                        "values-${env.name}.yaml",
                    ],
                ]
            )
            env.withCredentials() {
                helmDeployApp.upgrade(["install": true, "force": true, "wait": true])
            }
        }
    }

    
    public notifyBuild(String buildStatus = 'STARTED') {
      // build status of null means successful
      buildStatus =  buildStatus ?: 'SUCCESSFUL'

      // Default values
      def colorName = 'RED'
      def colorCode = '#FF0000'
        
      this.allEnvironments().each { env ->  
      def subject = "${buildStatus}: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'"
      def summary = "${subject} (${env.BUILD_URL})"
      def details = """<p>STARTED: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]':</p>
        <p>Check console output at &QUOT;<a href='${env.BUILD_URL}'>${env.JOB_NAME} [${env.BUILD_NUMBER}]</a>&QUOT;</p>"""
      }
        
      // Override default values based on build status
      if (buildStatus == 'STARTED') {
        color = 'YELLOW'
        colorCode = '#FFFF00'
      } else if (buildStatus == 'SUCCESSFUL') {
        color = 'GREEN'
        colorCode = '#00FF00'
      } else {
        color = 'RED'
        colorCode = '#FF0000'
      }

      // Send notifications
      slackSend (color: colorCode, message: summary)

    }
    
    
    public updateSecret(def secretName, def secretFile) {
        this.allEnvironments().each { env ->
            env.withCredentials() {
              Config.pipeline.sh ("""
                #kubectl create secret generic --from-file=$secretFile $secretName --dry-run -o yaml | kubectl apply -f -

                if kubectl get secret $secretName; then
                  kubectl delete secret $secretName
                fi
                kubectl create secret generic --from-file=$secretFile $secretName
              """)
            }
        }
    }
}
