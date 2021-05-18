package com.vojtechruzicka.javafxweaverexample.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Set;


public class Car {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("brand")
    private String brand;
    @JsonProperty("owner")
    private User owner;
    @JsonProperty("active")
    private boolean active;

    @JsonProperty("scope")
    private Integer scope;


    public Car(String brand, User owner) {
        this.active = true;
        this.owner = owner;
        this.brand = brand;
    }

    public Car(){}

    public Integer getScope() {
        return scope;
    }

    public void setScope(Integer scope) {
        this.scope = scope;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
