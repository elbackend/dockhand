package com.el.jenkins.pipeline.deploy

import com.boxboat.jenkins.pipeline.deploy.BoxDeploy

class ElDeploy extends BoxDeploy {

    ElDeploy(Map config = [:]) {
        super(config)
        setPropertiesFromMap(config)
    }
}
