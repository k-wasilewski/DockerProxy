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
    CountryRepository countryRepository;
    @Autowired
    DockerProxy dockerProxy;
    public static boolean isReady = false;

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
    public void onApplicationEvent(ContextRefreshedEvent event) {
        isReady = true;
    }
}