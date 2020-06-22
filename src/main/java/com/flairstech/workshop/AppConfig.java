package com.flairstech.workshop;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Lazy
@Configuration
@ComponentScan(basePackages = "com.flairstech.workshop")
public class AppConfig {

    @Bean
    public CountryLanguage getCountryLanguage(){
        return new CountryLanguage();
    }

    @Bean
    public Country getCountry(){
        return new Country();
    }
}
