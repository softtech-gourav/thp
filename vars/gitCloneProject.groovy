// vars/gitCloneProject.groovy

def call(String projectName, String branchName) {
    echo "Cloning project: ${projectName} from branch: ${branchName}"

    // Define the Git repository URL based on the project name
    def repoUrl = "https://bitbucket.org/groovenexus/${projectName}.git"

    // Perform the clone operation
    git branch: "${branchName}",
        credentialsId: 'bitbucket-credentials',
        url: repoUrl
}
