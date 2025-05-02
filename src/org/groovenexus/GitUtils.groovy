def getLatestGitTag() {
    try {
        // Make sure tags are fetched
        steps.sh("git fetch --tags")

        def latestCommit = steps.sh(script: "git rev-list --tags --max-count=1", returnStdout: true).trim()

        if (!latestCommit) {
            steps.echo "⚠️ No tags found in the repository. Using default version."
            return "v0.0.1"
        }

        def latestTag = steps.sh(script: "git describe --tags ${latestCommit}", returnStdout: true).trim()
        steps.echo "Latest Git Tag: ${latestTag}"
        return latestTag
    } catch (Exception e) {
        steps.error "Error fetching latest Git tag: ${e.message}"
        return 'v0.0.1'
    }
}
