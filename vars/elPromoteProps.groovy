def props() {
    def promotions = [
            "",
            "stage",
            "prod",
    ]
    return [
            parameters([
                    choice(name: 'promotionKey', choices: promotions, description: 'Promotion', defaultValue: ''),
                    string(name: 'overrideEvent', description: 'Override promote from event, typically commit/<branch>, tag/<tag>, or imageTag/build-0123456789ab', defaultValue: ''),
                    booleanParam(name: 'trigger', description: 'Skip Prompt', defaultValue: false),
                    string(name: 'overrideBranch', description: 'Override branch', defaultValue: ''),
                    string(name: 'overrideCommit', description: 'Override commit, commit must exist in branch', defaultValue: ''),
            ]),
            [$class: 'BuildDiscarderProperty', strategy: [$class: 'LogRotator', numToKeepStr: '100']]
    ]

}
