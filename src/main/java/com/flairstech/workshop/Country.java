package com.flairstech.workshop;

import javax.persistence.*;

@Entity
@Table(name = "country")
public class Country {
    @Id
    @Column(name = "capital")
    private int capital;

    @Column(name="code", columnDefinition = "bpchar")
    private String code;

    @Column(name="name")
    private String name;

    @Column(name="continent")
    private String continent;

    @Column(name="population", columnDefinition = "int4")
    private int population;

    @Column(name="life_expectancy", columnDefinition = "float4")
    private int lifeExpectancy;

    @Column(name="country_language")
    private String countryLanguage;

    public int getCapital() {
        return capital;
    }

    public void setCapital(int capital) {
        this.capital = capital;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContinent() {
        return continent;
    }

    public void setContinent(String continent) {
        this.continent = continent;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public int getLifeExpectancy() {
        return lifeExpectancy;
    }

    public void setLifeExpectancy(int lifeExpectancy) {
        this.lifeExpectancy = lifeExpectancy;
    }

    public String getCountryLanguage() {
        return countryLanguage;
    }

    public void setCountryLanguage(String countryLanguage) {
        this.countryLanguage = countryLanguage;
    }
}
