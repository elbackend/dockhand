package com.el.jenkins.pipeline.deploy

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
}
