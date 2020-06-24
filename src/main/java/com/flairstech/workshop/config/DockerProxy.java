package com.flairstech.workshop.config;

import org.springframework.stereotype.Component;

@Component
public class DockerProxy {
    public final static String CHECK_DOCKER_VERSION_CMD = "docker --version";
    public final static String CHECK_DOCKER_VERSION_EXCEPTION = "Docker is not installed";
    public final static String CHECK_DOCKER_PORT_AVAILABILITY_CMD = "docker ps --filter expose=5432 --format {{.Ports}}";
    public final static String CHECK_DOCKER_PORT_AVAILABILITY_EXCEPTION = "Port 5432 is already in use by another Docker container";
    public final static String CHECK_PORT_AVAILABILITY_CMD = "lsof -i:5432";
    public final static String CHECK_PORT_AVAILABILITY_EXCEPTION = "Port 5432 is already in use";
    public final static String DOCKER_ERROR = "Unresolved Docker error";
    public final static String RUN_DOCKER_IMAGE_CMD = "docker run -d -p 5432:5432 ghusta/postgres-world-db:2.4";
    public final static String RUN_DOCKER_IMAGE_EXCEPTION = "Exception during running database Docker image";
    public final static String IS_CONTAINER_RUNNING_CMD = "docker inspect -f \"{{.State.Running}}\" ";
    public final static String IS_CONTAINER_RUNNING_EXCEPTION = "Failed to run the Docker container";
    private final static String STOP_DOCKER_CONTAINER_CMD = "docker stop ";
    public final static String STOP_DOCKER_CONTAINER_EXCEPTION = "Exception during stopping database Docker container";
    private final static String REMOVE_DOCKER_CONTAINER_CMD = "docker rm ";
    public final static String REMOVE_DOCKER_CONTAINER_EXCEPTION = "Exception during removing database Docker container";

    public static String getIsContainerRunningCmd(String dockerContainer) {
        return String.format(IS_CONTAINER_RUNNING_CMD+"%s", dockerContainer);
    }

    public static String getStopDockerContainerCmd(String dockerContainer) {
        return String.format(STOP_DOCKER_CONTAINER_CMD+"%s", dockerContainer);
    }

    public static String getRemoveDockerContainerCmd(String dockerContainer) {
        return String.format(REMOVE_DOCKER_CONTAINER_CMD+"%s", dockerContainer);
    }
}
