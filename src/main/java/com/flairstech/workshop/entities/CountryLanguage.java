package com.flairstech.workshop.entities;

import javax.persistence.*;

@Entity
@Table(name = "country_language")
public class CountryLanguage {
    @Id
    @Column(name = "country_code", columnDefinition = "bpchar")
    private String countryCode;

    @Column(name = "language")
    private String language;

    public String getCountryCode() {
        return countryCode;
    }

    public String getLanguage() {
        return language;
    }
}
