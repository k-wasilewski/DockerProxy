package com.flairstech.workshop.controllers;

import com.flairstech.workshop.repositories.CountryLanguageRepository;
import com.flairstech.workshop.repositories.CountryRepository;
import com.flairstech.workshop.entities.Country;
import com.flairstech.workshop.entities.CountryLanguage;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.LinkedHashMap;
import java.util.Map;

@Controller
public class CountryController {
    @Autowired
    CountryRepository countryRepository;
    @Autowired
    CountryLanguageRepository countryLanguageRepository;

    @GetMapping(value = "/{code}")
    public @ResponseBody ResponseEntity<Object> getCountry(@PathVariable String code) {
        Country country;
        CountryLanguage countryLanguage;
        try {
            country = countryRepository.findByCode(code);
            countryLanguage = countryLanguageRepository.findByCountryCode(code);
        } catch (NoSuchBeanDefinitionException e) {
            return new ResponseEntity<>("INTERNAL_ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Map<String, Object> JSON = convertToJSON(country, countryLanguage);
        if (JSON==null) {
            return new ResponseEntity<>("INVALID_COUNTRY_CODE", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(JSON, HttpStatus.OK);
    }

    private Map<String, Object> convertToJSON(Country country, CountryLanguage countryLanguage) {
        Map<String, Object> countryJSON = new LinkedHashMap<>();

        try {
            countryJSON.put("name", country.getName());
            countryJSON.put("continent", country.getContinent());
            countryJSON.put("population", country.getPopulation());
            countryJSON.put("life_expectancy", country.getLifeExpectancy());
            countryJSON.put("country_language", countryLanguage.getLanguage());
        } catch (NullPointerException e) {
            return null;
        }

        return countryJSON;
    }
}
