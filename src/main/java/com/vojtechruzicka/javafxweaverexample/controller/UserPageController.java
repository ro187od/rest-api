package com.vojtechruzicka.javafxweaverexample.controller;

import com.vojtechruzicka.javafxweaverexample.model.Car;
import com.vojtechruzicka.javafxweaverexample.repo.CarsRepo;
import com.vojtechruzicka.javafxweaverexample.repo.UserRepo;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@FxmlView("user-page.fxml")
public class UserPageController extends DefaultJavaFXController  {

    public static final String TITLE = "Пользовательское окно";

    @Autowired
    private CarsRepo carsRepo;

    @Autowired
    private UserRepo userRepo;

    @FXML
    private Button logoutButton;

    @FXML
    private TextField filterFieldMyCars;

    @FXML
    private TextField filterField;

    @FXML
    private TableView<Car> tableMyCars;

    @FXML
    private TableView<Car> tableAllCars;

    @FXML
    private TableColumn<Car, Integer> idColumn;

    @FXML
    private TableColumn<Car, String> brandColumn;

    @FXML
    private TableColumn<Car, Long> serialNumberColumn;

    @FXML
    private TableColumn<Car, String> parkingAutoBrandColumn;

    @FXML
    private TableColumn<Car, String> ownerUsername;

    @FXML
    private TableColumn<Car, String> scope;


    @FXML
    private void initialize() {
        initRepo();

        ObservableList<Car> myCars = carsRepo.getMyCars();
        ObservableList<Car> carsInParkong = carsRepo.getCarsDataActiveCars();

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        brandColumn.setCellValueFactory(new PropertyValueFactory<>("brand"));
        serialNumberColumn.setCellValueFactory(new PropertyValueFactory<>("serialNumber"));
        parkingAutoBrandColumn.setCellValueFactory(new PropertyValueFactory<>("brand"));
        scope.setCellValueFactory(new PropertyValueFactory<>("scope"));
        ownerUsername.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getOwner().getUsername()));
        brandColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        FilteredList<Car> filteredDataMyCars = getCarFilteredList(myCars, filterFieldMyCars);
        FilteredList<Car> filteredData = getCarFilteredList(carsInParkong, filterField);

        SortedList<Car> sortedDataMyCars = getSortedList(filteredDataMyCars);
        SortedList<Car> sortedData = getSortedList(filteredData);

        tableMyCars.setItems(sortedDataMyCars);
        tableAllCars.setItems(sortedData);
        tableMyCars.setEditable(true);
    }

    private SortedList<Car> getSortedList(FilteredList<Car> filteredData) {
        SortedList<Car> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableMyCars.comparatorProperty());
        return sortedData;
    }

    @FXML
    private void handleDeleteCar() {
        deleteCar(tableMyCars);
    }

    @FXML
    public void onEditChange(TableColumn.CellEditEvent<Car, String> carStringCellEditEvent) {
        updateCar(carStringCellEditEvent, tableMyCars);
    }

    @FXML
    private void arithmeticMean() {
        showNewStageWindow(ArithmeticMeanController.class);
    }

    @FXML
    public void handleCreateCar() {
        showNewStageWindow(CreateCarController.class);
    }

    @FXML
    private void deactivateParking() {
        Car car = tableAllCars.getSelectionModel().getSelectedItem();
        EvaluationController.setCar(car);
        showNewStageWindow(EvaluationController.class);
    }

    @FXML
    private void activateParking() {
        Car car = tableMyCars.getSelectionModel().getSelectedItem();
        Map<String, String> params=  new HashMap<String, String>();
        params.put("id", String.valueOf(tableMyCars.getSelectionModel().getSelectedItem().getId()));
        restClient.put(REST_SERVER_URL + "car/activate/{id}", car, params);
        initRepo();
    }

    public void logout(ActionEvent actionEvent) {
        Stage stage = (Stage) logoutButton.getScene().getWindow();
        stage.close();
        showNewStageWindow(LoginController.class);
    }

}
