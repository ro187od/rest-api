package com.vojtechruzicka.javafxweaverexample.controller;

import com.vojtechruzicka.javafxweaverexample.model.Car;
import com.vojtechruzicka.javafxweaverexample.model.Parking;
import com.vojtechruzicka.javafxweaverexample.model.User;
import com.vojtechruzicka.javafxweaverexample.repo.CarsRepo;
import com.vojtechruzicka.javafxweaverexample.repo.UserList;
import com.vojtechruzicka.javafxweaverexample.repo.UserRepo;
import com.vojtechruzicka.javafxweaverexample.service.AlertService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

@Component
@FxmlView("admin-page.fxml")
public class AdminPageController extends DefaultJavaFXController {

    protected final static String TITLE = "Окно урпавляющего автостоянки";

    @FXML
    private TextField jTextField9;
    @FXML
    private TextField jTextField5;
    @FXML
    private TextField jTextField7;
    @FXML
    private TextField jTextField8;
    @FXML
    private TextField jTextField6;
    @FXML
    private TextField jTextField4;
    @FXML
    private TextField jTextField10;
    @FXML
    private ComboBox<Integer> jSpinner6;
    @FXML
    private ComboBox<Integer> jSpinner5;
    @FXML
    private ComboBox<Integer> jSpinner4;
    @FXML
    private ComboBox<Integer> jSpinner3;
    @FXML
    private ComboBox<Integer> jSpinner2;
    @FXML
    private ComboBox<Integer> jSpinner1;

    private HashMap<String, Double> map;

    private ValueComparator bvc;

    private TreeMap<String, Double> sorted_map;

    @Autowired
    private UserList userList;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private CarsRepo carsRepo;

    @FXML
    private Button logoutButton;



    @FXML
    private TextField filterFieldCar;

    @FXML
    private TextField filterFieldUser;

    @FXML
    private TextField filterFieldAdmin;

    @FXML
    private TableView<Car> tableAllCars;

    @FXML
    private TableView<User> tableAllUsers;

    @FXML
    public TableColumn<User, String> usernameAdmin;

    @FXML
    public TableColumn<User, String> moneyUser;

    @FXML
    private TableView<User> tableAllAdmins;

    @FXML
    private TableColumn<Car, String> serialColumn;

    @FXML
    private TableColumn<Car, String> brandColumn;

    @FXML
    private TableColumn<Car, String> ownerUsernameColumn;

    @FXML
    public TableColumn<User, String> username;

    @FXML
    public TableColumn<User, String> idColumn;

    @FXML
    public TableColumn<User, String> userPassword;

    @FXML
    public TableColumn<User, String> numberMachines;

    private Parking parking;

    @FXML
    private void initialize() {
        initRepo();

        parking = restClient.getForObject(REST_SERVER_URL + "parking/1", Parking.class);

        brandColumn.setCellValueFactory(new PropertyValueFactory<Car, String>("brand"));
        serialColumn.setCellValueFactory(new PropertyValueFactory<>("serialNumber"));
        ownerUsernameColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getOwner().getUsername()));

        numberMachines.setCellValueFactory(new PropertyValueFactory<User, String>("numberMachines"));
        usernameAdmin.setCellValueFactory(new PropertyValueFactory<User, String>("username"));
        username.setCellValueFactory(new PropertyValueFactory<User, String>("username"));
        moneyUser.setCellValueFactory(new PropertyValueFactory<User, String>("moneyInAccount"));
        userPassword.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getPassword()));
        idColumn.setCellValueFactory(new PropertyValueFactory<User, String>("id"));

        brandColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        serialColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        username.setCellFactory(TextFieldTableCell.forTableColumn());
        usernameAdmin.setCellFactory(TextFieldTableCell.forTableColumn());

        FilteredList<Car> filteredData = getCarFilteredList(carsRepo.getCarsDataParkingCars(), filterFieldCar);
        FilteredList<User> filteredDataUser = getCarFilteredListUser(userRepo.getRoleUserData(), filterFieldUser);
        FilteredList<User> filteredDataAdmin = getCarFilteredListUser(userRepo.getRoleAdminData(), filterFieldAdmin);

        SortedList<Car> sortedData = getSortedList(filteredData, tableAllCars);
        SortedList<User> sortedDataUser = getSortedListUser(filteredDataUser);
        SortedList<User> sortedDataAdmin = getSortedListUser(filteredDataAdmin);

        tableAllCars.setItems(sortedData);
        tableAllUsers.setItems(sortedDataUser);
        tableAllAdmins.setItems(sortedDataAdmin);
        tableAllCars.setEditable(true);
        tableAllUsers.setEditable(true);

        map = new HashMap<>();
        bvc = new ValueComparator(map);
        sorted_map = new TreeMap<>(bvc);
    }

    private FilteredList<User> getFilteredDataUser(ObservableList<User> users, TextField textField) {
        FilteredList<User> filteredData = new FilteredList<>(users, p -> true);

        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(car -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if (car.getUsername().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });
        return filteredData;
    }


    private FilteredList<User> getCarFilteredListUser(ObservableList<User> usersData, TextField filterFieldUser) {
        FilteredList<User> filteredData = getFilteredDataUser(usersData, filterFieldUser);
        return filteredData;
    }

    private SortedList<User> getSortedListUser(FilteredList<User> filteredData) {
        SortedList<User> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableAllUsers.comparatorProperty());
        return sortedData;
    }



    @FXML
    private void handleDeleteUser() {
        deleteUserOrAdmin(tableAllUsers);
    }

    @FXML
    private void handleDeleteAdmin() {
        deleteUserOrAdmin(tableAllAdmins);
    }

    private void deleteUserOrAdmin(TableView<User> table) {
        try{
            Map<String, String> params = new HashMap<String, String>();
            params.put("id", String.valueOf(table.getSelectionModel().getSelectedItem().getId()));
            String.valueOf(table.getSelectionModel().getSelectedItem());
            restClient.delete(REST_SERVER_URL + "users/{id}", params);
            initRepo();
        }catch (Exception e){
            alertService.showAlert(AlertService.AlertType.NO_CONNECTED);
        }

    }

    @FXML
    public void onEditChangeUser(TableColumn.CellEditEvent<Car, String> carStringCellEditEvent) {
        try{
            User user = tableAllUsers.getSelectionModel().getSelectedItem();
            user.setUsername(carStringCellEditEvent.getNewValue());
            Map<String, String> params = new HashMap<String, String>();
            params.put("id", String.valueOf(user.getId()));
            restClient.put(REST_SERVER_URL + "user/update/{id}", user, params);
        }catch (Exception e){
            alertService.showAlert(AlertService.AlertType.NO_CONNECTED);
        }
    }

    @FXML
    public void onEditChange(TableColumn.CellEditEvent<Car, String> carStringCellEditEvent) {
        updateCar(carStringCellEditEvent, tableAllCars);
    }

    public void logout(ActionEvent actionEvent) {
        Stage stage = (Stage) logoutButton.getScene().getWindow();
        stage.close();
        showNewStageWindow(LoginController.class);
    }

    @FXML
    public void handleCreateCar() {
        showNewStageWindow(CreateCarController.class);
    }

    @FXML
    public void handleCreateUser() {
        showNewStageWindow(CreateUserController.class);
    }

    @FXML
    public void handleCreateAdmin() {
        showNewStageWindow(CreateAdminController.class);
    }

    @FXML
    private void handleDeleteCar() {
        deleteCar(tableAllCars);
    }


    public void calculate(ActionEvent actionEvent) {
        int z1z2 = (int) jSpinner1.getValue();
        int z1z3 = (int) jSpinner2.getValue();
        int z2z1 = (int) jSpinner3.getValue();
        int z2z3 = (int) jSpinner4.getValue();
        int z3z1 = (int) jSpinner5.getValue();
        int z3z2 = (int) jSpinner6.getValue();
        if (z1z2 == z2z1 || z1z3 == z3z1 || z2z3 == z3z2) {
            alertService.showAlert(AlertService.AlertType.ERROR_METHOD);
        } else {
            int C1 = z1z2 + z1z3;
            int C2 = z2z1 + z2z3;
            int C3 = z3z1 + z3z2;
            DecimalFormatSymbols s = new DecimalFormatSymbols();
            s.setDecimalSeparator('.');
            DecimalFormat f = new DecimalFormat("0.00", s);
            double W1 = (double) C1 / (C1 + C2 + C3);
            double W2 = (double) C2 / (C1 + C2 + C3);
            double W3 = (double) C3 / (C1 + C2 + C3);
            W1 = Double.valueOf(f.format(W1));
            W2 = Double.valueOf(f.format(W2));
            W3 = Double.valueOf(f.format(W3));
            jTextField5.setText(String.valueOf(C1));
            jTextField8.setText(String.valueOf(C2));
            jTextField4.setText(String.valueOf(C3));
            jTextField9.setText(String.valueOf(W1));
            jTextField7.setText(String.valueOf(W2));
            jTextField6.setText(String.valueOf(W3));
            map.put("Z1", W1);
            map.put("Z2", W2);
            map.put("Z3", W3);
            if(C1 > C2 && C1 > C3){
                addFloorParking();
                alertService.showAlert(AlertService.AlertType.EXPAND_PARKING);
            }else if(C2 > C1 && C2 > C3){
                saleParking();
                alertService.showAlert(AlertService.AlertType.COST_BY_TWO);
            }else if(C3 > C1 && C3 > C2){
                expandParking();
                alertService.showAlert(AlertService.AlertType.EXPAND_PARKING_N_SEATS);
            }else{
                alertService.showAlert(AlertService.AlertType.SIMILAR_GOALS);
            }
            if (W1 == W2 & W2 == W3) {
                String answer = "{Z1=" + W1 + ", Z2=" + W2 +", Z3=" + W3
                        + "}";
                jTextField10.setText(answer);
            } else {
                sorted_map.putAll(map);
                jTextField10.setText(String.valueOf(sorted_map));
            }
        }
    }

    private void addFloorParking() {
        try{
            restClient.put(REST_SERVER_URL + "parking/add_floor", parking);
        }catch (Exception e){
            alertService.showAlert(AlertService.AlertType.NO_CONNECTED);
        }
    }

    private void expandParking() {
        try{
            restClient.put(REST_SERVER_URL + "parking/expand", parking);
        }catch (Exception e){
            alertService.showAlert(AlertService.AlertType.NO_CONNECTED);
        }
    }

    private void saleParking() {

        try{
            restClient.put(REST_SERVER_URL + "parking/sale", parking);
        }catch (Exception e){
            alertService.showAlert(AlertService.AlertType.NO_CONNECTED);
        }
    }



    @FXML
    public void descriptionMethod(ActionEvent actionEvent) {
            showNewStageWindow(InfoMethodController.class);
    }
}