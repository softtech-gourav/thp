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

// Cleanup Docker Resources
def cleanupDockerResources(CONTAINER_NAME, DOCKER_REGISTRY, DOCKER_IMAGE, FULL_IMAGE_NAME) {
    return {
        script {
            sh(script: "docker stop ${CONTAINER_NAME} || true", returnStatus: true)
            sh(script: "docker rm ${CONTAINER_NAME} || true", returnStatus: true)
            
            // Clean up the previous image tags
            sh """
                LAST_TAG=\$(docker images --filter=reference="${DOCKER_REGISTRY}/${DOCKER_IMAGE}:*" \\
                    --format '{{.Tag}}' | sort -r | head -n 1)
                if [ -n "\$LAST_TAG" ]; then
                    echo "Removing previous image with tag: \$LAST_TAG"
                    docker rmi ${DOCKER_REGISTRY}/${DOCKER_IMAGE}:\$LAST_TAG || true
                fi
            """

            sh(script: "docker rmi ${FULL_IMAGE_NAME} || true", returnStatus: true)
            sh(script: "docker image prune -f", returnStatus: true)
        }
    }
}
