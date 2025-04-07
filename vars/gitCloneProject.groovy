def call(String repoName, String branch = 'main') {
    git branch: branch,
        credentialsId: 'github',  // Use your Jenkins Git credentials ID here
        url: "https://github.com/softtech-gourav/${repoName}.git"
}
