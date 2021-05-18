package com.vojtechruzicka.javafxweaverexample.controller;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

@Component
@FxmlView("result.fxml")
public class ArithmeticMeanController extends DefaultJavaFXController {

    @FXML
    private javafx.scene.control.Button closeButton;
    @FXML private javafx.scene.control.Label resultId;

    public void initialize(){
        Integer result = restTemplate.getForObject("http://localhost:8082/arithmetic/mean", Integer.class);
        resultId.setText("Средняя оценка = " + String.valueOf(result));
    }

    public void kk(){
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
}


