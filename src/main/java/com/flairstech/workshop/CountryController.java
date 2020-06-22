package com.flairstech.workshop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

@Controller
public class CountryController {
    @Autowired
    CountryRepository countryRepository;
    @Autowired
    CountryLanguageRepository countryLanguageRepository;

    @GetMapping(value = "/{code}")
    public @ResponseBody
    Map<String, Object> getCountry(@PathVariable String code) {
        Country country = countryRepository.findByCode(code);
        CountryLanguage countryLanguage = countryLanguageRepository.findByCountryCode(code);

        return convertToJSON(country, countryLanguage);
    }

    private Map<String, Object> convertToJSON(Country country, CountryLanguage countryLanguage) {
        Map<String, Object> countryJSON = new LinkedHashMap<>();

        countryJSON.put("name", country.getName());
        countryJSON.put("continent", country.getContinent());
        countryJSON.put("population", country.getPopulation());
        countryJSON.put("life_expectancy", country.getLifeExpectancy());
        countryJSON.put("country_language", countryLanguage.getLanguage());

        return countryJSON;
    }
}
