package org.groovenexus

class GitUtils implements Serializable {
    def steps

    GitUtils(steps) {
        this.steps = steps
    }

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
            return 'v0.0.0' // fallback
        }
    }
}
