package com.vojtechruzicka.javafxweaverexample.controller;

import com.vojtechruzicka.javafxweaverexample.model.Car;
import com.vojtechruzicka.javafxweaverexample.model.Role;
import com.vojtechruzicka.javafxweaverexample.model.User;
import com.vojtechruzicka.javafxweaverexample.repo.CarsRepo;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@FxmlView("form_auto.fxml")
public class CreateCarController extends DefaultJavaFXController{

    @Autowired
    private CarsRepo carsRepo;

    @FXML
    private TextField idCars;

    @FXML
    private TextField brandCars;

    @FXML
    public Button saveButtom;

    @FXML
    private TextField serialNubmerCars;

    @FXML
    public void save(){
        User user = new User("user", "123", Role.USER);
        Car car = new Car(13, brandCars.getText(), serialNubmerCars.getText(), user);
        if(car != null){
            carsRepo.usersData.add(car);
            carsRepo.myCars(user);
            Stage stage = (Stage) saveButtom.getScene().getWindow();
            stage.close();
        }
    }

}
