//src/org/groovenexus/GitUtils.groovy

package org.groovenexus

class GitUtils implements Serializable {
    def steps

    GitUtils(steps) {
        this.steps = steps
    }

    def cloneFromGit(String repoUrl, String branch = 'main') {
        steps.git branch: branch, credentialsId: 'github', url: repoUrl
    }
}
