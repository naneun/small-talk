package com.naneun.smalltalk.container;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public abstract class MySQLTestContainer {

    private static final String DOCKER_IMAGE_NAME = "mysql:8.0.23";
    private static final String DATABASE_NAME = "testDB";
    private static final String USER_NAME = "root";
    private static final String PASSWORD = "";
    private static final String COMMAND = "--character-set-server=utf8mb4 --collation-server=utf8mb4_unicode_ci";

    public static final GenericContainer<?> MYSQL_CONTAINER = new MySQLContainer<>(DOCKER_IMAGE_NAME)
            .withDatabaseName(DATABASE_NAME)
            .withUsername(USER_NAME)
            .withPassword(PASSWORD)
            .withCommand(COMMAND)
            .withReuse(true);
}
