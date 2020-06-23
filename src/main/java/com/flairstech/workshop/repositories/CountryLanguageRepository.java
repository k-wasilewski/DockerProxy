package com.flairstech.workshop.repositories;

import com.flairstech.workshop.entities.CountryLanguage;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Lazy
@Repository
public interface CountryLanguageRepository extends JpaRepository<CountryLanguage, Integer> {
    CountryLanguage findByCountryCode(String countryCode);
}
