def props() {
    deployments = [
            "",
            "dev",
            "stage",
            "prod",
    ]
    return [
            parameters([
                    choice(name: 'deploymentKey', choices: deployments, description: 'Deployment', defaultValue: ''),
                    booleanParam(name: 'trigger', description: 'Skip Prompt', defaultValue: false),
            ]),
            [$class: 'BuildDiscarderProperty', strategy: [$class: 'LogRotator', numToKeepStr: '100']]
    ]

}
