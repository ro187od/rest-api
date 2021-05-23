package com.vojtechruzicka.javafxweaverexample.controller;

import com.vojtechruzicka.javafxweaverexample.model.Car;
import com.vojtechruzicka.javafxweaverexample.repo.CarsRepo;
import com.vojtechruzicka.javafxweaverexample.repo.UserRepo;
import com.vojtechruzicka.javafxweaverexample.service.AlertService;
import com.vojtechruzicka.javafxweaverexample.service.ValidationService;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public abstract class DefaultJavaFXController {

    protected final static String REST_SERVER_URL = "http://localhost:8083/";
    protected final static String TITLE = "Новое окно";
    protected static int PARKINZ_SIZE = 2;

    @Autowired
    private CarsRepo carsRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    protected FxWeaver fxWeaver;
    @Autowired
    protected RestTemplate restClient;
    @Autowired
    protected ValidationService validationService;
    @Autowired
    protected AlertService alertService;

    private Stage primaryStage;

    public void showCurrentStageWindow(Class<? extends DefaultJavaFXController> controllerClass, String title) {
        showStage(controllerClass, title, primaryStage);
    }

    public void showNewStageWindow(Class<? extends DefaultJavaFXController> controllerClass) {
        try {
            showStage(controllerClass, getControllerTitle(controllerClass), new Stage());
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    private void showStage(Class<? extends DefaultJavaFXController> controllerClass, String title, Stage stage) {
        Parent root = fxWeaver.loadView(controllerClass);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle(title);
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

    protected FilteredList<Car> getCarFilteredList(ObservableList<Car> cars, TextField textField) {
        FilteredList<Car> filteredData = getFilteredData(cars, textField);
        return filteredData;
    }

    protected SortedList<Car> getSortedList(FilteredList<Car> filteredData, TableView<Car> table) {
        SortedList<Car> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(table.comparatorProperty());
        return sortedData;
    }

    public void setStage(Stage stage) {
        this.primaryStage = stage;
    }

    private String getControllerTitle(Class<? extends DefaultJavaFXController> controllerClass)
            throws IllegalAccessException, NoSuchFieldException {
        return controllerClass.getDeclaredField("TITLE").get(this).toString();
    }

    protected HttpHeaders getJsonHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    void deleteCar(TableView<Car> tableMyCars) {
        Map< String, String > params = new HashMap<String,String>();
        params.put("id", String.valueOf(tableMyCars.getSelectionModel().getSelectedItem().getId()));
        String.valueOf(tableMyCars.getSelectionModel().getSelectedItem());
        restClient.delete(REST_SERVER_URL + "car/{id}", params);
        initRepo();
    }

    void updateCar(
            TableColumn.CellEditEvent<Car, String> carStringCellEditEvent,
            TableView<Car> tableAllCars) {
        Car car = tableAllCars.getSelectionModel().getSelectedItem();
        car.setBrand(carStringCellEditEvent.getNewValue());
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", String.valueOf(car.getId()));
        restClient.put(REST_SERVER_URL + "car/update/{id}", car, params);
    }

    protected void initRepo(){
        carsRepo.init();
        userRepo.init();

    }
}
