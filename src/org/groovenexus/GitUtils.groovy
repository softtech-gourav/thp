// src/org/groovenexus/GitUtils.groovy
package org.groovenexus

class GitUtils implements Serializable {
    def steps
    String bitbucketBaseUrl
    
    GitUtils(steps, Map config = [:]) {
        this.steps = steps
        this.bitbucketBaseUrl = config.baseUrl ?: "https://bitbucket.org/thppython/"
    }

    def cloneFromGit(String projectName, Map config = [:]) {
        // Validate inputs
        if (!projectName?.trim()) {
            steps.error("Project name must be specified")
        }
        
        String repoUrl = "${bitbucketBaseUrl}${projectName}.git"
        String branch = config.branch ?: 'main'
        String credentials = config.credentialsId ?: 'github'
        
        steps.echo "Cloning repository: ${repoUrl} (branch: ${branch})"
        
        try {
            if (config.useCheckout) {
                steps.checkout([
                    $class: 'GitSCM',
                    branches: [[name: branch]],
                    extensions: [[$class: 'CleanCheckout']],
                    userRemoteConfigs: [[
                        url: repoUrl,
                        credentialsId: credentials
                    ]]
                ])
            } else {
                steps.git(
                    url: repoUrl,
                    credentialsId: credentials,
                    branch: branch
                )
            }
        } catch (Exception e) {
            steps.error("Failed to clone repository: ${e.message}")
        }
    }
}
