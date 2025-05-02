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
            // ✅ Fetch all tags
            steps.sh("git fetch --tags")
        } catch (Exception e) {
            steps.error "Failed to clone repository: ${e.message}"
        }
    }

    // ✅ Optional: Get the latest tag
    def getLatestGitTag() {
        try {
            def latestTag = steps.sh(
                script: "git describe --tags `git rev-list --tags --max-count=1`",
                returnStdout: true
            ).trim()
            steps.echo "Latest Git Tag: ${latestTag}"
            return latestTag
        } catch (Exception e) {
            steps.error "Error fetching latest Git tag: ${e.message}"
            return 'v0.0.0' // fallback version
        }
    }
}
