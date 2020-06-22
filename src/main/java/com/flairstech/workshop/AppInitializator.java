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
    String container;

    @PostConstruct
    private void init() {
        try {
            container = executeBashCommand("docker run -d -p 5432:5432 ghusta/postgres-world-db:2.4");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        System.out.println(countryRepository.findById(1));
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

        return cmdOutput[0];
    }

    @PreDestroy
    private void destr() {
        if (container!=null)
            try {
                executeBashCommand("docker stop "+container);
                executeBashCommand("docker remove "+container);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
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
