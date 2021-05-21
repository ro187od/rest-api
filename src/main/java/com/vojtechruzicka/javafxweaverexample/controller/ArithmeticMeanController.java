package com.vojtechruzicka.javafxweaverexample.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

@Component
@FxmlView("result.fxml")
public class ArithmeticMeanController extends DefaultJavaFXController {

    protected final static String TITLE = "Результат метода экспертной оценки";

    @FXML
    private Label metricResult;

    @FXML
    public void initialize() {
        setMetricResultFromServer();
    }

    private void setMetricResultFromServer() {
        Integer result = restClient.getForObject(REST_SERVER_URL + "arithmetic/mean", Integer.class);
        metricResult.setText(String.valueOf(result));
    }
}


