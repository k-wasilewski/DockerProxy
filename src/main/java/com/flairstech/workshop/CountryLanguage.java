package com.flairstech.workshop;

import javax.persistence.*;

@Entity
@Table(name = "country_language")
public class CountryLanguage {
    @Id
    @Column(name = "country_code", columnDefinition = "bpchar")
    String countryCode;

    @Column(name = "language")
    String language;

    public String getCountryCode() {
        return countryCode;
    }

    public String getLanguage() {
        return language;
    }
}
