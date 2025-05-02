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
            url: repoUrl,
            changelog: false,
            poll: false
        )

        // 🔁 Fetch all tags after checkout
        steps.sh("git fetch --tags")
    } catch (Exception e) {
        steps.error "Failed to clone repository: ${e.message}"
    }
}
