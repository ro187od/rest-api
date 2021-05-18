package com.vojtechruzicka.javafxweaverexample.controller;

import com.sun.org.apache.xpath.internal.operations.Bool;
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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
@FxmlView("form_auto.fxml")
public class CreateCarController extends DefaultJavaFXController{

    @Autowired
    private CarsRepo carsRepo;

    @FXML
    private TextField brandCars;

    @FXML
    public Button saveButtom;


    @FXML
    public void save(){
        if(brandCars.getText().length() != 0){
            User user = restTemplate.getForObject("http://localhost:8082/users/my", User.class);
            Car car = new Car(brandCars.getText(), user);
            HttpEntity<Car> requestBody = new HttpEntity<>(car, getHttpHeaders());
            Boolean result = restTemplate.postForObject(
                    "http://localhost:8082/car/create", requestBody, Boolean.class);
            if(car != null){
                carsRepo.setCars();
                carsRepo.setMyCars();
                Stage stage = (Stage) saveButtom.getScene().getWindow();
                stage.close();
            }
        }else{
            System.out.println("Вы не ввели марку автомобиля");
            return;
        }
    }
    private HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}
