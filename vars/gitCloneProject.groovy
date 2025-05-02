def cloneFromGit(String projectName, String branch = 'main') {
    if (!projectName) {
        throw new IllegalArgumentException("Project name cannot be empty")
    }

    String repoUrl = "${bitbucketBaseUrl}${projectName}.git"
    steps.echo "Cloning from: ${repoUrl}"

    try {
        steps.checkout([
            $class: 'GitSCM',
            branches: [[name: "*/${branch}"]],
            userRemoteConfigs: [[
                url: repoUrl,
                credentialsId: 'bitbucket-credentials'
            ]],
            extensions: [
                [$class: 'CloneOption', noTags: false, shallow: false]
            ]
        ])
    } catch (Exception e) {
        steps.error "Failed to clone repository: ${e.message}"
    }
}
