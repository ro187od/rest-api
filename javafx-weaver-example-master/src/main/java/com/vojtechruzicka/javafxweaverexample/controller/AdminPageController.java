package com.vojtechruzicka.javafxweaverexample.controller;

import com.vojtechruzicka.javafxweaverexample.model.Car;
import com.vojtechruzicka.javafxweaverexample.model.User;
import com.vojtechruzicka.javafxweaverexample.repo.CarsRepo;
import com.vojtechruzicka.javafxweaverexample.repo.UserList;
import com.vojtechruzicka.javafxweaverexample.repo.UserRepo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@FxmlView("admin-page.fxml")
public class AdminPageController extends DefaultJavaFXController {

    private final String uri = "http://localhost:8082/users";

    @Autowired
    private UserList userList;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private CarsRepo carsRepo;

    @FXML
    private TextField filterField;

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
    private TableView<User> tableAllAdmins;

    @FXML
    private TableColumn<Car, Integer> idColumn;

    @FXML
    private TableColumn<Car, String> serialColumn;

    @FXML
    private TableColumn<Car, String> brandColumn;

    @FXML
    public TableColumn<User, String> username;
    @FXML

    private void initialize() {
        userRepo.init();
        userRepo.init();
        carsRepo.init();
        ObservableList<User> users = userRepo.getRoleUserData();

        ObservableList<User> admins = userRepo.getRoleAdminData();

        ObservableList<Car> cars = carsRepo.getCars();

        idColumn.setCellValueFactory(new PropertyValueFactory<Car, Integer>("id"));
        brandColumn.setCellValueFactory(new PropertyValueFactory<Car, String>("brand"));
        serialColumn.setCellValueFactory(new PropertyValueFactory<Car, String>("serial_number"));

        username.setCellValueFactory(new PropertyValueFactory<User, String>("username"));

        usernameAdmin.setCellValueFactory(new PropertyValueFactory<User, String>("username"));

        brandColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        serialColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        username.setCellFactory(TextFieldTableCell.forTableColumn());

        usernameAdmin.setCellFactory(TextFieldTableCell.forTableColumn());

        FilteredList<Car> filteredData = getCarFilteredList(carsRepo.getCarsDataActiveCars(), filterField);

        SortedList<Car> sortedData = getSortedList(filteredData);


        FilteredList<User> filteredDataUser = getCarFilteredListUser(users, filterFieldUser);

        FilteredList<User> filteredDataAdmin = getCarFilteredListUser(admins, filterFieldAdmin);

        SortedList<User> sortedDataUser = getSortedListUser(filteredDataUser);

        SortedList<User> sortedDataAdmin = getSortedListUser(filteredDataAdmin);

        tableAllCars.setItems(sortedData);
        tableAllUsers.setItems(sortedDataUser);
        tableAllAdmins.setItems(sortedDataAdmin);
        tableAllCars.setEditable(true);
        tableAllUsers.setEditable(true);
    }



    private FilteredList<User> getCarFilteredListUser(ObservableList<User> usersData, TextField filterFieldUser) {
        FilteredList<User> filteredData = getFilteredDataUser(usersData, filterFieldUser);
        return filteredData;
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

    private SortedList<Car> getSortedList(FilteredList<Car> filteredData) {
        SortedList<Car> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableAllCars.comparatorProperty());
        return sortedData;
    }

    private SortedList<User> getSortedListUser(FilteredList<User> filteredData) {
        SortedList<User> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableAllUsers.comparatorProperty());
        return sortedData;
    }



    @FXML
    public void handleCreateCar() {
        redirect(CreateCarController.class);
    }

    @FXML
    public void handleCreateUser() {
        redirect(CreateUserController.class);
    }

    @FXML
    public void handleCreateAdmin() {
        redirect(CreateAdminController.class);
    }



    @FXML
    private void handleDeleteCar() {
        Map< String, String > params = new HashMap<String,String>();
        params.put("id", String.valueOf(tableAllCars.getSelectionModel().getSelectedItem().getId()));
        String.valueOf(tableAllCars.getSelectionModel().getSelectedItem());
        restTemplate.delete("http://localhost:8082/car/{id}", params);
        carsRepo.init();
    }

    @FXML
    private void handleDeleteUser() {
        Map< String, String > params = new HashMap<String,String>();
        params.put("id", String.valueOf(tableAllUsers.getSelectionModel().getSelectedItem().getId()));
        String.valueOf(tableAllUsers.getSelectionModel().getSelectedItem());
        restTemplate.delete("http://localhost:8082/users/{id}", params);
        userRepo.init();
    }

    @FXML
    private void handleDeleteAdmin() {
        Map< String, String > params = new HashMap<String,String>();
        params.put("id", String.valueOf(tableAllAdmins.getSelectionModel().getSelectedItem().getId()));
        String.valueOf(tableAllAdmins.getSelectionModel().getSelectedItem());
        restTemplate.delete("http://localhost:8082/users/{id}", params);
        userRepo.init();
    }

    @FXML
    public void onEditChange(TableColumn.CellEditEvent<Car, String> carStringCellEditEvent) {
        Car car = tableAllCars.getSelectionModel().getSelectedItem();
        car.setBrand(carStringCellEditEvent.getNewValue());
        Map<String, String> params=  new HashMap<String, String>();
        params.put("id", String.valueOf(car.getId()));
        restTemplate.put("http://localhost:8082/car/update/{id}", car, params);
    }

    @FXML
    public void onEditChangeUser(TableColumn.CellEditEvent<Car, String> carStringCellEditEvent) {
        User user = tableAllUsers.getSelectionModel().getSelectedItem();
        user.setUsername(carStringCellEditEvent.getNewValue());
        Map<String, String> params=  new HashMap<String, String>();
        params.put("id", String.valueOf(user.getId()));
        restTemplate.put("http://localhost:8082/user/update/{id}", user, params);
    }
}
