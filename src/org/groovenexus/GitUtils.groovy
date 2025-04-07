package org.groovenexus

class GitUtils implements Serializable {
    def steps  // Required to use Jenkins steps like `git`

    GitUtils(steps) {
        this.steps = steps
    }

    def cloneFromBitbucket(String projectName, String branch = 'main') {
        def repoUrl = "https://digvijaynath@bitbucket.org/thppython/${projectName}.git"
        steps.git branch: branch, url: repoUrl
    }
}
