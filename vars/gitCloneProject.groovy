def call() {
    sh "git fetch --tags"

    def latestCommit = sh(script: "git rev-list --tags --max-count=1", returnStdout: true).trim()

    if (!latestCommit) {
        echo "⚠️ No tags found. Using fallback version."
        return "v0.0.1"
    }

    def tag = sh(script: "git describe --tags ${latestCommit}", returnStdout: true).trim()
    echo "✅ Latest Git tag is: ${tag}"
    return tag
}
