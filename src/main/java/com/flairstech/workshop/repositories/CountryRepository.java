package com.flairstech.workshop.repositories;

import com.flairstech.workshop.entities.Country;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Lazy
@Repository
public interface CountryRepository extends JpaRepository<Country, Integer> {
    Country findByCode(String code);
}
