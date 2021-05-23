package com.vojtechruzicka.javafxweaverexample.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Car {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("brand")
    private String brand;
    @JsonProperty("owner")
    private User owner;
    @JsonProperty("active")
    private boolean active;
    @JsonProperty("serialCarNumber")
    private String serialNumber;
    @JsonProperty("scope")
    private Integer scope;
    @JsonProperty("parking")
    private Parking parking;

    public Car(String brand, String serialNumber, User owner) {
        this.active = true;
        this.owner = owner;
        this.brand = brand;
        this.serialNumber = serialNumber;
    }
}
