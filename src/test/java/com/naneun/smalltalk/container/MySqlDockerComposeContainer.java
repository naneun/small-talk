package com.naneun.smalltalk.container;

import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;

@Deprecated
@Testcontainers
public class MySqlDockerComposeContainer {

    private static final String DOCKER_COMPOSE_YML_PATH = "src/test/resources/docker-compose.yml";

    public static final DockerComposeContainer DOCKER_COMPOSE_CONTAINER =
            new DockerComposeContainer(new File(DOCKER_COMPOSE_YML_PATH));

    // TODO Optimizing Docker Compose. DOCKER_COMPOSE_CONTAINER.start();
}
