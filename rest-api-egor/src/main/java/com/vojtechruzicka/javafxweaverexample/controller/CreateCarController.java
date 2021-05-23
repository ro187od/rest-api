package com.vojtechruzicka.javafxweaverexample.controller;

import com.vojtechruzicka.javafxweaverexample.model.Car;
import com.vojtechruzicka.javafxweaverexample.model.User;
import com.vojtechruzicka.javafxweaverexample.repo.CarsRepo;
import com.vojtechruzicka.javafxweaverexample.service.AlertService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;

@Component
@FxmlView("form_auto.fxml")
public class CreateCarController extends DefaultJavaFXController{

    protected final static String TITLE = "Добавление новой машины";

    @Autowired
    private CarsRepo carsRepo;

    @FXML
    private TextField textFieldBrandCars;

    @FXML
    private TextField textFieldSerialNumberCar;

    @FXML
    public Button saveButtomCar;


    @FXML
    public void save(){
        String brand = textFieldBrandCars.getText();
        String serialNumber = textFieldSerialNumberCar.getText();

        if(!validationService.validateEmptyLines(brand, serialNumber)) {
            alertService.showAlert(AlertService.AlertType.BRAND_IS_EMPTY);
        }else{
            User user = restClient.getForObject(REST_SERVER_URL + "users/my", User.class);
            Car car = new Car(brand, serialNumber, user);
            HttpEntity<Car> requestBody = new HttpEntity<>(car, getJsonHttpHeaders());
            Boolean result = restClient.postForObject(
                    REST_SERVER_URL + "car/create", requestBody, Boolean.class);
            carsRepo.setCars();
            carsRepo.setMyCars();
            initRepo();
            Stage stage = (Stage) saveButtomCar.getScene().getWindow();
            stage.close();
        }
    }
}

