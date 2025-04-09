// src/org/groovenexus/GitUtils.groovy
package org.groovenexus

class GitUtils implements Serializable {
    def steps
    String bitbucketBaseUrl = "https://digvijaynath@bitbucket.org/thppython/"

    GitUtils(steps) {
        this.steps = steps
    }

    def cloneFromGit(String projectName, String branch = 'main') {
        String repoUrl = "${bitbucketBaseUrl}${projectName}.git"
        steps.git branch: branch, credentialsId: 'bitbucket-credentials', url: repoUrl
    }
}
