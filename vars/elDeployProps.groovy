def props() {
    deployments = [
            "",
            "dev",
            "stage",
            "test-prod",

    ]
    return [
            parameters([
                    choice(name: 'deploymentKey', choices: deployments, description: 'Deployment', defaultValue: ''),
            ]),
            [$class: 'BuildDiscarderProperty', strategy: [$class: 'LogRotator', numToKeepStr: '100']]
    ]

}
