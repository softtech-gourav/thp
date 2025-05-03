package org.groovenexus

class GitUtils implements Serializable {
    def steps
    String bitbucketBaseUrl = "https://bitbucket.org/thppython/"

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

    /**
     * Fetch latest Git tag from repo.
     * Returns: String (e.g., "v1.0.3") or "v0.0.0" if no tags found.
     */
    def getLatestGitTag() {
        steps.sh "git fetch --tags"

        def latestCommit = steps.sh(
            script: "git rev-list --tags --max-count=1",
            returnStdout: true
        ).trim()

        if (!latestCommit) {
            steps.echo "No tags found. Defaulting to v0.0.0"
            return "v0.0.0"
        }

        def tag = steps.sh(
            script: "git describe --tags ${latestCommit}",
            returnStdout: true
        ).trim()

        return tag
    }
}
