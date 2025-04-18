// src/org/groovenexus/GitUtils.groovy
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
        steps.echo "Cloning from: ${repoUrl}" // Debugging line

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
}
