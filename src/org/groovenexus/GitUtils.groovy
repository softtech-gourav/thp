package org.groovenexus

class GitUtils implements Serializable {
    def steps
    String bitbucketBaseUrl = "https://bitbucket.org/thppython/" // ✅ Fix this

    GitUtils(steps) {
        this.steps = steps
    }

    def cloneFromGit(String projectName, String branch = 'main') {
        if (!projectName) {
            throw new IllegalArgumentException("Project name cannot be empty")
        }

        String repoUrl = "${bitbucketBaseUrl}${projectName}.git"
        steps.echo "📥 Cloning from: ${repoUrl}" // Debug log

        try {
            steps.git(
                branch: branch,
                credentialsId: 'bitbucket-credentials',
                url: repoUrl
            )
        } catch (Exception e) {
            steps.error "❌ Failed to clone repository: ${e.message}"
        }
    }

    def getLatestGitTag() {
        steps.sh "git fetch --tags"
        def latestCommit = steps.sh(script: "git rev-list --tags --max-count=1", returnStdout: true).trim()
        if (!latestCommit) {
            steps.echo "⚠️ No tags found. Using fallback version."
            return "v0.0.1"
        }
        def tag = steps.sh(script: "git describe --tags ${latestCommit}", returnStdout: true).trim()
        steps.echo "✅ Latest Git tag is: ${tag}"
        return tag
    }
}
