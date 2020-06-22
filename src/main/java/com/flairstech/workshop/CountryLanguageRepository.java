package com.flairstech.workshop;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryLanguageRepository extends JpaRepository<CountryLanguage, Integer> {
    CountryLanguage findByCountryCode(String countryCode);
}
