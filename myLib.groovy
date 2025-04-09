// vars/gitCloneProject.groovy
def call(String projectName, String branch = 'main') {
    def gitUtils = new org.groovenexus.GitUtils(steps)
    gitUtils.cloneFromGit(projectName, branch)
}
