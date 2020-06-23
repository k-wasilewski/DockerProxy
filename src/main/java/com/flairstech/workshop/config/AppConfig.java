package com.flairstech.workshop.config;

import com.flairstech.workshop.entities.Country;
import com.flairstech.workshop.entities.CountryLanguage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.config.BootstrapMode;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.transaction.TransactionManager;
import java.util.Properties;

@Lazy
@Configuration
@ComponentScan(basePackages = "com.flairstech.workshop")
@EnableJpaRepositories(bootstrapMode = BootstrapMode.LAZY, basePackages = "com.flairstech.workshop.repositories")
public class AppConfig {

    @Bean
    public CountryLanguage getCountryLanguage(){
        return new CountryLanguage();
    }

    @Bean
    public Country getCountry(){
        return new Country();
    }

    @Bean
    public EntityManager getEntityManager() {
        return entityManagerFactory().createEntityManager();
    }

    @Bean(name = "entityManagerFactory")
    public EntityManagerFactory entityManagerFactory() {
        Properties props = new Properties();
        return Persistence.createEntityManagerFactory("prsstnc");
    }

    @Bean
    public PlatformTransactionManager transactionManager(){
        JpaTransactionManager transactionManager
                = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory());
        return transactionManager;
    }

    @Bean
    public TransactionTemplate transactionTemplate() {
        return new TransactionTemplate(transactionManager());
    }
}
