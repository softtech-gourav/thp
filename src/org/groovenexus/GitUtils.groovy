package org.groovenexus

class GitUtils implements Serializable {
    def steps
    String bitbucketBaseUrl = "https://bitbucket.org/thppython/"  // ✅ Make sure this matches your actual repo path

    GitUtils(steps) {
        this.steps = steps
    }

    def cloneFromGit(String projectName, String branch = 'main') {
        if (!projectName) {
            throw new IllegalArgumentException("Project name cannot be empty")
        }

        String repoUrl = "${bitbucketBaseUrl}${projectName}.git"
        steps.echo "Cloning from: ${repoUrl}"

        try {
            steps.git(
                branch: branch,
                credentialsId: 'bitbucket-credentials',
                url: repoUrl
            )
        } catch (Exception e) {
            steps.error "Failed to clone repository: ${e.message}"
        }
    }

    def getLatestGitTag() {
        try {
            def result = steps.sh(
                script: 'git fetch --tags && git describe --tags `git rev-list --tags --max-count=1`',
                returnStdout: true
            ).trim()
            return result ?: 'v1.0.0'
        } catch (Exception e) {
            steps.echo "Failed to get latest Git tag: ${e.message}"
            return 'v1.0.0'
        }
    }
}
