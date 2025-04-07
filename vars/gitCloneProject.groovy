def call(String projectName, String branch = 'main') {
    def gitUtils = new org.groovenexus.GitUtils(this)
    gitUtils.cloneFromBitbucket(projectName, branch)
}
