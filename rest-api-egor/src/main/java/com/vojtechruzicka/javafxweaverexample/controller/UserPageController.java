package com.vojtechruzicka.javafxweaverexample.controller;

import com.vojtechruzicka.javafxweaverexample.model.Car;
import com.vojtechruzicka.javafxweaverexample.model.Parking;
import com.vojtechruzicka.javafxweaverexample.model.User;
import com.vojtechruzicka.javafxweaverexample.repo.CarsRepo;
import com.vojtechruzicka.javafxweaverexample.repo.UserRepo;
import com.vojtechruzicka.javafxweaverexample.service.AlertService;
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

    private Parking parking;

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

    private User activeUser;

    @FXML
    private void initialize() {
        initRepo();

        parking = restClient.getForObject(REST_SERVER_URL + "parking/3", Parking.class);

        ObservableList<Car> myCars = carsRepo.getMyCars();
        ObservableList<Car> carsInParkong = carsRepo.getCarsDataParkingCars();

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

        activeUser = userRepo.getMyUser();
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
    public void handleCreateCar() {
        showNewStageWindow(CreateCarController.class);
    }

    @FXML
    private void deactivateParking() {
        try{
            Car car = tableAllCars.getSelectionModel().getSelectedItem();
            if(!car.getOwner().getUsername().equals(activeUser.getUsername())){
                alertService.showAlert(AlertService.AlertType.ACCESS_ERROR);
            }else{
                Map< String, String > params = new HashMap<String,String>();
                params.put("id_parking","3");
                params.put("id_car", String.valueOf(car.getId()));
                restClient.delete(REST_SERVER_URL + "parking/{id_parking}/car/{id_car}", params);
                EvaluationController.setCar(car);
                showNewStageWindow(EvaluationController.class);
            }
        }catch (Exception e){
            alertService.showAlert(AlertService.AlertType.NO_CONNECTED);
        }
    }

    @FXML
    private void activateParking() {
        try{
            if(parking.getSizeParking() > parking.getCars().size()){
                Car car = tableMyCars.getSelectionModel().getSelectedItem();
                if(car.getOwner().getMoneyInAccount() < parking.getCostParking()){
                    alertService.showAlert(AlertService.AlertType.PARKING_ERROR_BALANCE);
                }else if(parking.getCars().indexOf(car.getId()) == car.getId()){
                    alertService.showAlert(AlertService.AlertType.CAR_ALREADY_IN_PARK);
                }else {
                    Map<String, String> params=  new HashMap<String, String>();
                    params.put("id", String.valueOf(tableMyCars.getSelectionModel().getSelectedItem().getId()));
                    restClient.put(REST_SERVER_URL + "parking/add/car/{id}", car, params);
                    initRepo();
                }
            }else{
                alertService.showAlert(AlertService.AlertType.PARKING_ERROR);
            }
        }catch (Exception e){
            alertService.showAlert(AlertService.AlertType.NO_CONNECTED);
        }

    }

    public void logout(ActionEvent actionEvent) {
        Stage stage = (Stage) logoutButton.getScene().getWindow();
        stage.close();
        showNewStageWindow(LoginController.class);
    }

    public void cashIn(ActionEvent actionEvent) {
        try {
            Map<String, String> params = new HashMap<String, String>();
            params.put("id", String.valueOf(activeUser.getId()));
            restClient.put(REST_SERVER_URL + "user/update/{id}", activeUser, params);
        }catch (Exception e){
            alertService.showAlert(AlertService.AlertType.NO_CONNECTED);
        }
    }
}
