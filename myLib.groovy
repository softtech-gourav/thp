// vars/gitCloneProject.groovy
def call(Map config = [:]) {
    def defaults = [
        projectName: '',
        branch: 'main',
        credentialsId: 'github',
        baseUrl: 'https://bitbucket.org/thppython/',
        useCheckout: false
    ]
    
    def cfg = defaults + config
    
    if (!cfg.projectName) {
        error("Project name must be specified")
    }
    
    def gitUtils = new org.groovenexus.GitUtils(steps, [baseUrl: cfg.baseUrl])
    gitUtils.cloneFromGit(cfg.projectName, [
        branch: cfg.branch,
        credentialsId: cfg.credentialsId,
        useCheckout: cfg.useCheckout
    ])
}
