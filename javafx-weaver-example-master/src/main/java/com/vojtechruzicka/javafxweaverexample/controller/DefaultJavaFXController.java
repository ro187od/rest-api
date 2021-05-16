package com.vojtechruzicka.javafxweaverexample.controller;

import com.vojtechruzicka.javafxweaverexample.model.Car;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import java.net.URI;


public abstract class DefaultJavaFXController {

    @Autowired
    protected RestTemplate restTemplate;
    @Autowired
    protected FxWeaver fxWeaver;

    private Stage primaryStage;

    protected DefaultJavaFXController() {}

    public void showCurrentStageWindow(Class<? extends DefaultJavaFXController> controllerClass, String title) {
        showStage(controllerClass, title, primaryStage);
    }

    public void showNewStageWindow(Class<? extends DefaultJavaFXController> controllerClass, String title) {
        showStage(controllerClass, title, new Stage());
    }

    private void showStage(Class<? extends DefaultJavaFXController> controllerClass, String title, Stage stage) {
        Parent root = fxWeaver.loadView(controllerClass);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle(title);
        stage.show();
    }

    public void redirect(Class<? extends DefaultJavaFXController> controllerClass) {
        Parent root = fxWeaver.loadView(controllerClass);
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public FilteredList<Car> getFilteredData(ObservableList<Car> cars, TextField textField) {
        FilteredList<Car> filteredData = new FilteredList<>(cars, p -> true);

        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(car -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if (car.getBrand().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });
        return filteredData;
    }

    public FilteredList<Car> getCarFilteredList(ObservableList<Car> cars, TextField textField) {
        FilteredList<Car> filteredData = getFilteredData(cars, textField);
        return filteredData;
    }


    public void hideCurrentStageWindow() {
        primaryStage.hide();
    }

    public void setStage(Stage stage) {
        this.primaryStage = stage;
    }
}
