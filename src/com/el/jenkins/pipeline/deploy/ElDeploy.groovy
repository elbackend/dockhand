package com.el.jenkins.pipeline.deploy

import com.boxboat.jenkins.pipeline.deploy.BoxDeploy
import com.boxboat.jenkins.pipeline.deploy.kubernetes.HelmDeploy

class ElDeploy extends BoxDeploy {

    ElDeploy(Map config = [:]) {
        super(config)
        setPropertiesFromMap(config)
    }

    public deploy() {
      this.writeImageTags(
        outFile: "./cicd/deploy/${app}/image-tags.yaml",
      )

      def releaseName = "${app}-${this.config.deploymentKey}"
      this.allEnvironments().each { env ->
            def helmDeployApp = new HelmDeploy(
                chart: ".",
                directory: "./cicd/deploy/${app}",
                name: releaseName,
                options: [
                    "set"      : [],
                    "values"   : [
                        "image-tags.yaml",
                        "values-${env.name}.yaml",
                    ],
                ]
            )
        }
    }
}
