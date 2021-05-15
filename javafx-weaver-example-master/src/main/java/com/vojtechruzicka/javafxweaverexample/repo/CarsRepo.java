package com.vojtechruzicka.javafxweaverexample.repo;

import com.vojtechruzicka.javafxweaverexample.controller.DefaultJavaFXController;
import com.vojtechruzicka.javafxweaverexample.model.Car;
import com.vojtechruzicka.javafxweaverexample.model.Role;
import com.vojtechruzicka.javafxweaverexample.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class CarsRepo extends DefaultJavaFXController {

    public ObservableList<Car> usersData = FXCollections.observableArrayList();
    public ObservableList<Car> usersDataActiveCars = FXCollections.observableArrayList();
    public ObservableList<Car> myCars = FXCollections.observableArrayList();

    public CarsRepo(){}

    public void initData(User user, User admin) {
        usersData.add(new Car(1, "Bmw", "123q3", user));
        usersData.add(new Car(2, "Audi", "123q3", user));;
        usersData.add(new Car(3, "Tesla", "123q33", admin));
        usersData.add(new Car(4, "Ford", "123q12", admin));
        usersData();
        myCars(user);
    }

    public void myCars(User user) {
        usersData.forEach(car -> {
            if (car.getUser() == user) {
                myCars.add(car);
            }
        });
    }

    public void usersData() {
        usersDataActiveCars.clear();
        usersData.forEach(car -> {
            if (car.isActiveParking() == true) {
                usersDataActiveCars.add(car);
            }
        });
    }

    public Car findById(int id, User user) {
        return usersDataActiveCars.stream().filter(car ->
                car.getId() == id && car.getUser() == user)
                .findFirst().map(car -> car).orElse(null);
    }
}
