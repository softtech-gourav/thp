// vars/dockerCleanup.groovy

def call(CONTAINER_NAME, DOCKER_REGISTRY, DOCKER_IMAGE, FULL_IMAGE_NAME) {
    sh(script: "docker stop ${CONTAINER_NAME} || true", returnStatus: true)
    sh(script: "docker rm ${CONTAINER_NAME} || true", returnStatus: true)

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
