// src/org/groovenexus/GitUtils.groovy
package org.groovenexus

class GitUtils implements Serializable {
    def steps
    String bitbucketBaseUrl = "https://bitbucket.org/thp-python/"

    GitUtils(steps) {
        this.steps = steps
    }

    def cloneFromGit(String projectName, String branch = 'main') {
        if (!projectName) {
            throw new IllegalArgumentException("Project name cannot be empty")
        }

        String repoUrl = "${bitbucketBaseUrl}${projectName}.git"
        steps.echo "📥 Cloning from: ${repoUrl}"

        try {
            steps.git(
                branch: branch,
                credentialsId: 'bitbucket-credentials',
                url: repoUrl
            )
            steps.sh 'git fetch --tags' // ✅ Ensure tags are fetched
        } catch (Exception e) {
            steps.error "❌ Failed to clone repository: ${e.message}"
        }
    }

    def getLatestGitTag() {
        try {
            def latestCommit = steps.sh(script: "git rev-list --tags --max-count=1", returnStdout: true).trim()

            if (!latestCommit) {
                steps.echo "⚠️ No tags found. Using fallback version."
                return "v0.0.1"
            }

            def tag = steps.sh(script: "git describe --tags ${latestCommit}", returnStdout: true).trim()
            steps.echo "✅ Latest Git tag is: ${tag}"
            return tag
        } catch (Exception e) {
            steps.echo "❌ Failed to get latest Git tag: ${e.message}"
            return "v0.0.1"
        }
    }
}
