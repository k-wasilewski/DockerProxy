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
import java.io.IOException;

@Component
public class AppInitializer {
    @Lazy
    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private DockerProxy dockerProxy;
    private static boolean ready = false;

    public static boolean isReady() {return ready;}

    private static void setReady(boolean isReady) {ready=isReady;}

    @PostConstruct
    private void init() throws IOException, InterruptedException, DockerProxyException {
        DockerProxy.handleDockerPreConstructExceptions();

        DockerProxy.runDockerImage();

        DockerProxy.handleDockerPostConstructExceptions();
    }

    @PreDestroy
    private void destr() throws DockerProxyException {
        DockerProxy.handleDockerPreDestroyExceptions();
    }

    @EventListener
    private void onApplicationEvent(ContextRefreshedEvent event) {
        setReady(true);
    }
}