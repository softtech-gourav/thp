def call(String projectName, String branch = 'main') {
    def gitUtils = new org.groovenexus.GitUtils(steps)
    try {
        gitUtils.cloneFromGit(projectName, branch)
    } catch (Exception e) {
        steps.error "Git Clone failed: ${e.message}"
    }
}
