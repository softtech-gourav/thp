import org.groovenexus.GitUtils

def call(String repoName, String branch = 'main') {
    def gitUtils = new GitUtils(this)
    def repoUrl = "https://github.com/softtech-gourav/${repoName}.git"
    gitUtils.cloneFromGit(repoUrl, branch)
}
