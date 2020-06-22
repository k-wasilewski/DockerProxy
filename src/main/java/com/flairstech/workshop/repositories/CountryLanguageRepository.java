package com.flairstech.workshop.repositories;

import com.flairstech.workshop.entities.CountryLanguage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryLanguageRepository extends JpaRepository<CountryLanguage, Integer> {
    CountryLanguage findByCountryCode(String countryCode);
}
