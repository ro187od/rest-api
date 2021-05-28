package com.vojtechruzicka.javafxweaverexample.controller;

import com.vojtechruzicka.javafxweaverexample.model.Car;
import com.vojtechruzicka.javafxweaverexample.repo.CarsRepo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@FxmlView("evaluation-form.fxml")
public class EvaluationController extends DefaultJavaFXController {

    protected final static String TITLE = "Поставьте оценку автостоянке";

    @Autowired
    private CarsRepo carsRepo;

    private static Car carFromDb;

    @FXML
    public Spinner<Integer> resultingAssessment;

    @FXML
    public Button saveButtonResult;

    public static void setCar(Car car) {
        carFromDb = car;
    }

    public void sendResult(ActionEvent actionEvent) {
        carFromDb.setScope(resultingAssessment.getValue());
        restClient.put(REST_SERVER_URL + "car/add/scope", carFromDb);
        carsRepo.init();
        Stage stage = (Stage) saveButtonResult.getScene().getWindow();
        stage.close();
    }
}
