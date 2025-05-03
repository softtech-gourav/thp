package org.groovenexus

class GitUtils implements Serializable {
    def steps
    String bitbucketBaseUrl = "https://bitbucket.org/thp-python/"  // ✅ Corrected base URL

    GitUtils(steps) {
        this.steps = steps
    }

    def getLatestGitTag() {
        try {
            def result = steps.sh(script: 'git fetch --tags && git describe --tags `git rev-list --tags --max-count=1`', returnStdout: true).trim()
            return result ?: 'v1.0.0'
        } catch (Exception e) {
            steps.echo "Failed to get latest Git tag: ${e.message}"
            return 'v1.0.0'
        }
    }

    def cloneFromGit(String projectName, String branch = 'main') {
        if (!projectName) {
            throw new IllegalArgumentException("Project name cannot be empty")
        }

        String repoUrl = "${bitbucketBaseUrl}${projectName}.git"  // Builds to: https://bitbucket.org/thp-python/gnadmin-frontend.git
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
}
