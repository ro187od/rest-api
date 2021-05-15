package com.vojtechruzicka.javafxweaverexample.controller;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;


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

    public void hideCurrentStageWindow() {
        primaryStage.hide();
    }

    public void setStage(Stage stage) {
        this.primaryStage = stage;
    }
}
