package com.vojtechruzicka.javafxweaverexample.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
public class Parking {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("carsList")
    private List<Car> cars;
    @JsonProperty("sizeParking")
    private Integer sizeParking;
    @JsonProperty("costParking")
    private Integer costParking;

    public Parking(ArrayList cars, Integer sizeParking, Integer costParking){
        this.sizeParking = sizeParking;
        this.cars = new ArrayList();
        this.costParking = costParking;
    }

    public void addCar(Car car){
        cars.add(car);
    }
}
