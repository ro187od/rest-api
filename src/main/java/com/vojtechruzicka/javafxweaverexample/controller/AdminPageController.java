package com.vojtechruzicka.javafxweaverexample.controller;

import com.vojtechruzicka.javafxweaverexample.model.Car;
import com.vojtechruzicka.javafxweaverexample.model.User;
import com.vojtechruzicka.javafxweaverexample.repo.CarsRepo;
import com.vojtechruzicka.javafxweaverexample.repo.UserList;
import com.vojtechruzicka.javafxweaverexample.repo.UserRepo;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
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
import javafx.util.Callback;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@FxmlView("admin-page.fxml")
public class AdminPageController extends DefaultJavaFXController {

    protected final static String TITLE = "Окно админа";

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

    @FXML
    private void initialize() {
        initRepo();

        brandColumn.setCellValueFactory(new PropertyValueFactory<Car, String>("brand"));
        serialColumn.setCellValueFactory(new PropertyValueFactory<>("serialNumber"));
            ownerUsernameColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getOwner().getUsername()));

        numberMachines.setCellValueFactory(new PropertyValueFactory<User, String>("numberMachines"));
        usernameAdmin.setCellValueFactory(new PropertyValueFactory<User, String>("username"));
        username.setCellValueFactory(new PropertyValueFactory<User, String>("username"));
        userPassword.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getPassword()));
        idColumn.setCellValueFactory(new PropertyValueFactory<User, String>("id"));

        brandColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        serialColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        username.setCellFactory(TextFieldTableCell.forTableColumn());
        usernameAdmin.setCellFactory(TextFieldTableCell.forTableColumn());

        FilteredList<Car> filteredData = getCarFilteredList(carsRepo.getCarsDataActiveCars(), filterFieldCar);
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
    private void handleDeleteCar() { deleteCar(tableAllCars); }

    @FXML
    private void handleDeleteUser() { deleteUserOrAdmin(tableAllUsers); }

    @FXML
    private void handleDeleteAdmin() { deleteUserOrAdmin(tableAllAdmins); }

    @FXML
    public void onEditChange(TableColumn.CellEditEvent<Car, String> carStringCellEditEvent) {
        updateCar(carStringCellEditEvent, tableAllCars);
    }

    private void deleteUserOrAdmin(TableView<User> table) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", String.valueOf(table.getSelectionModel().getSelectedItem().getId()));
        String.valueOf(table.getSelectionModel().getSelectedItem());
        restClient.delete(REST_SERVER_URL + "users/{id}", params);
        initRepo();
    }

    @FXML
    public void onEditChangeUser(TableColumn.CellEditEvent<Car, String> carStringCellEditEvent) {
        User user = tableAllUsers.getSelectionModel().getSelectedItem();
        user.setUsername(carStringCellEditEvent.getNewValue());
        Map<String, String> params=  new HashMap<String, String>();
        params.put("id", String.valueOf(user.getId()));
        restClient.put(REST_SERVER_URL + "user/update/{id}", user, params);
    }

    public void logout(ActionEvent actionEvent) {
        Stage stage = (Stage) logoutButton.getScene().getWindow();
        stage.close();
        showNewStageWindow(LoginController.class);
    }


}