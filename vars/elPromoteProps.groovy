def props() {
  def promotions = [
            "",
            "stage",
            "prod",
    ]
    return [
            parameters([
                    choice(name: 'promotionKey', choices: promotions, description: 'Promotion', defaultValue: ''),
            ]),
            [$class: 'BuildDiscarderProperty', strategy: [$class: 'LogRotator', numToKeepStr: '100']]
    ]

}
