package com.flairstech.workshop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

@Component
public class AppInitializator {
    @Autowired
    CountryRepository countryRepository;

    //Docker proxy configuration
    String container;
    final String CHECK_DOCKER_PORT_AVAILABILITY_CMD = "docker ps --filter \"expose=5432\"";
    final String CHECK_DOCKER_PORT_AVAILABILITY_EXCEPTION = "Port 5432 is already in use by another Docker container";
    final String CHECK_PORT_AVAILABILITY_CMD = "lsof -i:5432";
    final String CHECK_PORT_AVAILABILITY_EXCEPTION = "Port 5432 is already in use";
    final String CONTAINER_IN_USE_EXCEPTION = "Container with name 'countriesdocker' is already in use";
    final String RUN_DOCKER_IMAGE_CMD = "docker run -d --name=countriesdocker -p 5432:5432 ghusta/postgres-world-db:2.4";
    final String RUN_DOCKER_IMAGE_EXCEPTION = "Exception during running database Docker image";
    final String STOP_DOCKER_CONTAINER_CMD = "docker stop "+container;
    final String STOP_DOCKER_CONTAINER_EXCEPTION = "Exception during stopping database Docker container";
    final String REMOVE_DOCKER_CONTAINER_CMD = "docker rm "+container;
    final String REMOVE_DOCKER_CONTAINER_EXCEPTION = "Exception during removing database Docker container";

    @PostConstruct
    private void init() throws Exception {
        if (executeBashCommand(CHECK_PORT_AVAILABILITY_CMD)!=null)
            throw new IOException(CHECK_PORT_AVAILABILITY_EXCEPTION);
        if (executeBashCommand(CHECK_DOCKER_PORT_AVAILABILITY_CMD)!=null)
            throw new IOException(CHECK_DOCKER_PORT_AVAILABILITY_EXCEPTION);

        try {
            container = executeBashCommand(RUN_DOCKER_IMAGE_CMD);
        } catch (Exception e) {
            throw new IOException(RUN_DOCKER_IMAGE_EXCEPTION);
        }
    }

    @PreDestroy
    private void destr() throws IOException {
        if (container!=null) {
            try {
                executeBashCommand(STOP_DOCKER_CONTAINER_CMD);
            } catch (Exception e) {
                throw new IOException(STOP_DOCKER_CONTAINER_EXCEPTION);
            }

            try {
                executeBashCommand(REMOVE_DOCKER_CONTAINER_CMD);
            } catch (Exception e) {
                throw new IOException(REMOVE_DOCKER_CONTAINER_EXCEPTION);
            }
        }
    }

    private String executeBashCommand(String command) throws IOException, InterruptedException {
        Process process;
        String[] cmdOutput = new String[1];

        process = Runtime.getRuntime().exec(command);

        StreamGobbler streamGobbler =
                new StreamGobbler(process.getInputStream(), (output) -> {
                    cmdOutput[0] = output;
                });
        Executors.newSingleThreadExecutor().submit(streamGobbler);
        int exitCode = process.waitFor();
        assert exitCode == 0;

        if (command.equals(RUN_DOCKER_IMAGE_CMD) && cmdOutput[0]==null)
            throw new IOException(CONTAINER_IN_USE_EXCEPTION);

        return cmdOutput[0];
    }

    private static class StreamGobbler implements Runnable {
        private InputStream inputStream;
        private Consumer<String> consumer;

        public StreamGobbler(InputStream inputStream, Consumer<String> consumer) {
            this.inputStream = inputStream;
            this.consumer = consumer;
        }

        @Override
        public void run() {
            new BufferedReader(new InputStreamReader(inputStream)).lines()
                    .forEach(consumer);
        }
    }
}