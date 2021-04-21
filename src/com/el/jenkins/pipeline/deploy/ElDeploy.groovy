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
                helmDeployApp.upgrade(["atomic": true,"install": true, "force": true,])
            }
        }
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
