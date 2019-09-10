def props() {
    deployments = [
            "",
            "dev",
            "stage",
            "prod",
    ]
    deployActions = [
            "update",
            "replace",
            "delete"
    ]
    return [
            parameters([
                    choice(name: 'deploymentKey', choices: deployments, description: 'Deployment', defaultValue: ''),
                    choice(name: 'deployAction', choices: deployActions, description: 'Deployment Action', defaultValue: 'update'),
                    string(name: 'triggerEvent', description: 'Event, typically commit/<branch>', defaultValue: ''),
                    booleanParam(name: 'trigger', description: 'Skip Prompt', defaultValue: false),
                    string(name: 'overrideBranch', description: 'Override branch', defaultValue: ''),
                    string(name: 'overrideCommit', description: 'Override commit, commit must exist in branch', defaultValue: ''),
            ]),
            [$class: 'BuildDiscarderProperty', strategy: [$class: 'LogRotator', numToKeepStr: '100']]
    ]

}
