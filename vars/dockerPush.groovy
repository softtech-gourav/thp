// vars/dockerPush.groovy

// Docker login function
def call(dockerImage) {
    def dockerRegistry = env.DOCKER_REGISTRY
    def dockerRepo = env.DOCKER_REPO
    def credentialsId = env.HARBOR_CREDENTIALS_ID

    if (!dockerRegistry || !dockerRepo || !credentialsId) {
        error "Required environment variables are not set."
    }

    docker.withRegistry("https://${dockerRegistry}", credentialsId) {
        dockerImage.push()
        dockerImage.push("${env.BUILD_ID}")
    }
}

// Run Docker Container
def runDockerContainer(CONTAINER_NAME, PORT_MAPPING, FULL_IMAGE_NAME) {
    return {
        script {
            sh """
            docker run --restart unless-stopped \\
                --name ${CONTAINER_NAME} \\
                -d \\
                -p ${PORT_MAPPING} \\
                ${FULL_IMAGE_NAME}
            """
        }
    }
}


