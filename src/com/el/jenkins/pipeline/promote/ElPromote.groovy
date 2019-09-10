package com.el.jenkins.pipeline.promote

import com.boxboat.jenkins.pipeline.deploy.BoxPromote

class ElPromote extends BoxPromote {

    ElDeploy(Map config = [:]) {
        super(config)
    }
}
