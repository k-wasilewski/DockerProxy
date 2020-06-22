package com.flairstech.workshop;

import javax.persistence.*;

@Entity
@Table(name = "country_language")
public class CountryLanguage {
    @Id
    @Column(name = "country_code")
    String countryCode;

    @Column(name = "language")
    String language;

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
