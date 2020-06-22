package com.flairstech.workshop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
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

    @PostConstruct
    private void init() {
        try {
            executeBashCommand();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        System.out.println(countryRepository.findById(1));
    }

    private void executeBashCommand() throws IOException, InterruptedException {
        Process process;

        process = Runtime.getRuntime()
                .exec("docker run -d -p 5432:5432 ghusta/postgres-world-db:2.4");

        StreamGobbler streamGobbler =
                new StreamGobbler(process.getInputStream(), System.out::println);
        Executors.newSingleThreadExecutor().submit(streamGobbler);
        int exitCode = process.waitFor();
        assert exitCode == 0;
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
