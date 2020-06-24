package com.flairstech.workshop.config;

import com.flairstech.workshop.exceptions.DockerProxyException;
import com.flairstech.workshop.repositories.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
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
    public static boolean isReady = false;
    public static boolean test = false;

    @PostConstruct
    private void init()
            throws IOException, InterruptedException, DockerProxyException {
        if (executeBashCommand(DockerProxy
                .CHECK_DOCKER_VERSION_CMD)==null)
            throw new DockerProxyException(DockerProxy
                .CHECK_DOCKER_VERSION_EXCEPTION);
        if (!test && executeBashCommand(DockerProxy
                .CHECK_DOCKER_PORT_AVAILABILITY_CMD)!=null)
            throw new DockerProxyException(DockerProxy
                    .CHECK_DOCKER_PORT_AVAILABILITY_EXCEPTION);
        if (!test && executeBashCommand(DockerProxy
                .CHECK_PORT_AVAILABILITY_CMD)!=null)
            throw new DockerProxyException(DockerProxy
                    .CHECK_PORT_AVAILABILITY_EXCEPTION);

        try {
            if (!test) dockerContainer = executeBashCommand(DockerProxy
                    .RUN_DOCKER_IMAGE_CMD);
        } catch (Exception e) {
            throw new DockerProxyException(DockerProxy
                    .RUN_DOCKER_IMAGE_EXCEPTION);
        }

        if (!test && executeBashCommand(DockerProxy.getIsContainerRunningCmd(dockerContainer))==null ||
                executeBashCommand(DockerProxy.getIsContainerRunningCmd(dockerContainer)).equals("false"))
            throw new DockerProxyException(DockerProxy.IS_CONTAINER_RUNNING_EXCEPTION);

        if (!test && dockerContainer==null)
            throw new DockerProxyException(DockerProxy.DOCKER_ERROR);
    }

    @PreDestroy
    private void destr() throws DockerProxyException {
        if (dockerContainer!=null) {
            try {
                executeBashCommand(DockerProxy
                        .getStopDockerContainerCmd(dockerContainer));
            } catch (Exception e) {
                throw new DockerProxyException(DockerProxy
                        .STOP_DOCKER_CONTAINER_EXCEPTION);
            }

            try {
                executeBashCommand(DockerProxy
                        .getRemoveDockerContainerCmd(dockerContainer));
            } catch (Exception e) {
                throw new DockerProxyException(DockerProxy
                        .REMOVE_DOCKER_CONTAINER_EXCEPTION);
            }
        }
    }

    private String executeBashCommand(String command)
            throws IOException, InterruptedException {
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

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        isReady = true;
    }
}