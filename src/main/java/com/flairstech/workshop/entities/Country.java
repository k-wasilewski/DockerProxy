package com.flairstech.workshop.entities;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

@Entity
@Table(name = "country")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Country {
    @Id
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

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getContinent() {
        return continent;
    }

    public int getPopulation() {
        return population;
    }

    public int getLifeExpectancy() {
        return lifeExpectancy;
    }
}
