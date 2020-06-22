package com.flairstech.workshop.repositories;

import com.flairstech.workshop.entities.Country;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<Country, Integer> {
    Country findByCode(String code);
}
