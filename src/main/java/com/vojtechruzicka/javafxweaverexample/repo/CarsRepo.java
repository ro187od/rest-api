package com.vojtechruzicka.javafxweaverexample.repo;

import com.vojtechruzicka.javafxweaverexample.controller.DefaultJavaFXController;
import com.vojtechruzicka.javafxweaverexample.model.Car;
import com.vojtechruzicka.javafxweaverexample.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CarsRepo extends DefaultJavaFXController {

    private ResponseEntity<List<Car>> carsData;
    private ObservableList<Car> carsDataActiveCars = FXCollections.observableArrayList();
    private ResponseEntity<List<Car>> myCars;
    private ObservableList<Car> carsMyData = FXCollections.observableArrayList();

    public CarsRepo(){}

    public void init(){
        setCars();
        setMyCars();
    }


    public void setMyCars() {
        carsMyData.clear();
        myCars =
                restTemplate.exchange("http://localhost:8082/cars/my",
                        HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<Car>>() {
                        });
        myCars.getBody().forEach(car -> {
                carsMyData.add(car);
        });
    }

    public void setCars() {
        carsDataActiveCars.clear();
        carsData =
                restTemplate.exchange("http://localhost:8082/cars",
                        HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<Car>>() {
                        });
        carsData.getBody().forEach(car -> {
            if (car.isActive() == true) {
                carsDataActiveCars.add(car);
            }
        });
    }


    public ObservableList<Car> getCars() {
        return carsDataActiveCars;
    }

    public ObservableList<Car> getMyCars() {
        return carsMyData;
    }

    public ObservableList<Car> getCarsDataActiveCars() {
        return carsDataActiveCars;
    }
}
