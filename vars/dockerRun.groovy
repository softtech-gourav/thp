// vars/dockerRun.groovy

def call(CONTAINER_NAME, PORT_MAPPING, FULL_IMAGE_NAME) {
    sh """
    docker run --restart unless-stopped \\
        --name ${CONTAINER_NAME} \\
        -d \\
        -p ${PORT_MAPPING} \\
        ${FULL_IMAGE_NAME}
    """
}
