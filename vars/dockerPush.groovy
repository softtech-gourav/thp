// vars/dockerPush.groovy

// Docker login
def call(dockerImage) {
    // Access environment variables
    def dockerRegistry = env.DOCKER_REGISTRY
    def dockerRepo = env.DOCKER_REPO
    def credentialsId = env.HARBOR_CREDENTIALS_ID

    // Ensure required environment variables are set
    if (!dockerRegistry || !dockerRepo || !credentialsId) {
        error "Required environment variables (DOCKER_REGISTRY, DOCKER_REPO, HARBOR_CREDENTIALS_ID) are not set."
    }

    // Perform Docker push within the registry context
    docker.withRegistry("http://${dockerRegistry}", credentialsId) {
        dockerImage.push()  // Push the image to the repository
        dockerImage.push("${env.BUILD_ID}")  // Tag the image with the build ID and push again
    }
}

// Run docker container
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

// Cleanup docker resources
def cleanupDockerResources(CONTAINER_NAME, DOCKER_REGISTRY, DOCKER_IMAGE, FULL_IMAGE_NAME) {
    return {
        script {
            // Stop and remove existing container
            sh(script: "docker stop ${CONTAINER_NAME} || true", returnStatus: true)
            sh(script: "docker rm ${CONTAINER_NAME} || true", returnStatus: true)

            // Clean up using registry-prefixed image name
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
