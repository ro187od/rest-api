package com.vojtechruzicka.javafxweaverexample.repo;

import com.vojtechruzicka.javafxweaverexample.controller.DefaultJavaFXController;
import com.vojtechruzicka.javafxweaverexample.model.Car;
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
                restClient.exchange(REST_SERVER_URL + "cars/my",
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
                restClient.exchange(REST_SERVER_URL + "cars",
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
