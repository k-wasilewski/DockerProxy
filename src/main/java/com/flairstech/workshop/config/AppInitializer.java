package com.flairstech.workshop.config;

import com.flairstech.workshop.repositories.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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
public class AppInitializer {
    @Lazy
    @Autowired
    CountryRepository countryRepository;
    @Autowired
    DockerProxy dockerProxy;
    private String dockerContainer;

    @PostConstruct
    private void init() throws Exception {
        if (executeBashCommand(DockerProxy.CHECK_DOCKER_PORT_AVAILABILITY_CMD)!=null)
            throw new IOException(DockerProxy.CHECK_DOCKER_PORT_AVAILABILITY_EXCEPTION);
        if (executeBashCommand(DockerProxy.CHECK_PORT_AVAILABILITY_CMD)!=null)
            throw new IOException(DockerProxy.CHECK_PORT_AVAILABILITY_EXCEPTION);

        try {
            dockerContainer = executeBashCommand(DockerProxy.RUN_DOCKER_IMAGE_CMD);
        } catch (Exception e) {
            throw new IOException(DockerProxy.RUN_DOCKER_IMAGE_EXCEPTION);
        }

        if (executeBashCommand(DockerProxy.getIsContainerRunningCmd(dockerContainer))==null ||
                executeBashCommand(DockerProxy.getIsContainerRunningCmd(dockerContainer)).equals("false"))
            throw new IOException(DockerProxy.IS_CONTAINER_RUNNING_EXCEPTION);

        if (dockerContainer==null)
            throw new IOException(DockerProxy.DOCKER_ERROR);
    }

    @PreDestroy
    private void destr() throws IOException {
        if (dockerContainer!=null) {
            try {
                executeBashCommand(DockerProxy.getStopDockerContainerCmd(dockerContainer));
            } catch (Exception e) {
                throw new IOException(DockerProxy.STOP_DOCKER_CONTAINER_EXCEPTION);
            }

            try {
                executeBashCommand(DockerProxy.getRemoveDockerContainerCmd(dockerContainer));
            } catch (Exception e) {
                throw new IOException(DockerProxy.REMOVE_DOCKER_CONTAINER_EXCEPTION);
            }
        }
    }

    private String executeBashCommand(String command) throws IOException, InterruptedException {
        Process process;
        String[] cmdOutput = new String[1];

        process = Runtime.getRuntime().exec(command);

        StreamGobbler streamGobbler = new StreamGobbler(process.getInputStream(), (output) -> {
                    cmdOutput[0] = output;
                });
        Executors.newSingleThreadExecutor().submit(streamGobbler);
        int exitCode = process.waitFor();

        if (command.equals(DockerProxy.RUN_DOCKER_IMAGE_CMD) && cmdOutput[0]==null)
            return DockerProxy.DOCKER_ERROR;
        if (!command.equals(DockerProxy.getIsContainerRunningCmd(dockerContainer)))
            assert exitCode == 0;

        return cmdOutput[0];
    }

    private static class StreamGobbler implements Runnable {
        private final InputStream inputStream;
        private final Consumer<String> consumer;

        public StreamGobbler(InputStream inputStream, Consumer<String> consumer) {
            this.inputStream = inputStream;
            this.consumer = consumer;
        }

        @Override
        public void run() {
            new BufferedReader(new InputStreamReader(inputStream)).lines().forEach(consumer);
        }
    }
}