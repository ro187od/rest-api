package com.vojtechruzicka.javafxweaverexample.controller;

import com.vojtechruzicka.javafxweaverexample.model.Car;
import com.vojtechruzicka.javafxweaverexample.model.User;
import com.vojtechruzicka.javafxweaverexample.repo.CarsRepo;
import com.vojtechruzicka.javafxweaverexample.repo.UserRepo;
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
import org.springframework.stereotype.Component;

@Component
@FxmlView("admin-page.fxml")
public class AdminPageController extends DefaultJavaFXController {

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
        userRepo.initData();

        User user = userRepo.usersData.get(0);
        User admin = userRepo.usersData.get(1);

        carsRepo.initData(user, admin);

        idColumn.setCellValueFactory(new PropertyValueFactory<Car, Integer>("id"));
        brandColumn.setCellValueFactory(new PropertyValueFactory<Car, String>("brand"));
        serialColumn.setCellValueFactory(new PropertyValueFactory<Car, String>("serial_number"));

        username.setCellValueFactory(new PropertyValueFactory<User, String>("username"));

        usernameAdmin.setCellValueFactory(new PropertyValueFactory<User, String>("username"));

        brandColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        serialColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        username.setCellFactory(TextFieldTableCell.forTableColumn());

        usernameAdmin.setCellFactory(TextFieldTableCell.forTableColumn());

        FilteredList<Car> filteredData = getCarFilteredList(carsRepo.usersData, filterField);

        SortedList<Car> sortedData = getSortedList(filteredData);


        FilteredList<User> filteredDataUser = getCarFilteredListUser(userRepo.roleUserData, filterFieldUser);

        FilteredList<User> filteredDataAdmin = getCarFilteredListUser(userRepo.roleAdminData, filterFieldAdmin);

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
        tableAllCars.setItems(carsRepo.usersData);
        tableAllCars.getItems().remove(tableAllCars.getSelectionModel().getSelectedItem());
    }

    @FXML
    private void handleDeleteUser() {
        tableAllUsers.setItems(userRepo.roleUserData);
        tableAllUsers.getItems().remove(tableAllUsers.getSelectionModel().getSelectedItem());
    }

    @FXML
    private void handleDeleteAdmin() {
        tableAllAdmins.setItems(userRepo.roleAdminData);
        tableAllAdmins.getItems().remove(tableAllAdmins.getSelectionModel().getSelectedItem());
    }

    @FXML
    public void onEditChange(TableColumn.CellEditEvent<Car, String> carStringCellEditEvent) {
        Car car = tableAllCars.getSelectionModel().getSelectedItem();
        car.setBrand(carStringCellEditEvent.getNewValue());
    }

    @FXML
    public void onEditChangeUser(TableColumn.CellEditEvent<Car, String> carStringCellEditEvent) {
        User user = tableAllUsers.getSelectionModel().getSelectedItem();
        user.setUsername(carStringCellEditEvent.getNewValue());
    }
}
