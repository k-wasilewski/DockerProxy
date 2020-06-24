package com.flairstech.workshop.entities;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

@Entity
@Table(name = "country_language")
public class CountryLanguage {
    @Id
    @Column(name = "country_code", columnDefinition = "bpchar")
    private String countryCode;

    @Column(name = "language")
    private String language;

    @Column(name = "is_official")
    private boolean isOfficial;

    public String getCountryCode() {
        return countryCode;
    }

    public String getLanguage() {
        return language;
    }

    public boolean getIsOfficial() {return isOfficial;}
}
