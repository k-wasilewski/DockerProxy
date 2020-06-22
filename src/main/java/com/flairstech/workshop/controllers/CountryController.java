package com.flairstech.workshop.controllers;

import com.flairstech.workshop.repositories.CountryLanguageRepository;
import com.flairstech.workshop.repositories.CountryRepository;
import com.flairstech.workshop.entities.Country;
import com.flairstech.workshop.entities.CountryLanguage;
import org.springframework.beans.factory.annotation.Autowired;
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
    public @ResponseBody Map<String, Object> getCountry(@PathVariable String code) {
        Country country = countryRepository.findByCode(code);
        CountryLanguage countryLanguage = countryLanguageRepository.findByCountryCode(code);

        Map<String, Object> JSON = convertToJSON(country, countryLanguage);
        if (JSON==null) {
            return new LinkedHashMap<String, Object>() {{
                put("error", "Country not found");
            }};
        }
        return JSON;
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
