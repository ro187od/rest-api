package com.vojtechruzicka.javafxweaverexample.controller;

import com.vojtechruzicka.javafxweaverexample.model.Car;
import com.vojtechruzicka.javafxweaverexample.model.User;
import com.vojtechruzicka.javafxweaverexample.repo.CarsRepo;
import com.vojtechruzicka.javafxweaverexample.repo.UserRepo;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@FxmlView("user-page.fxml")
public class UserPageController extends DefaultJavaFXController  {
    public static final String TITLE = "UserPage";

    @Autowired
    private CarsRepo carsRepo;

    @Autowired
    private UserRepo userRepo;

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
    private TableColumn<Car, String> serialColumn;

    @FXML
    private TableColumn<Car, String> brandColumn;

    @FXML
    private TableColumn<Car, String> parkingAutoBrandColumn;


    @FXML
    private void initialize() {
        userRepo.initData();

        User user = userRepo.usersData.get(0);
        User admin = userRepo.usersData.get(1);

        carsRepo.initData(user, admin);

        idColumn.setCellValueFactory(new PropertyValueFactory<Car, Integer>("id"));
        brandColumn.setCellValueFactory(new PropertyValueFactory<Car, String>("brand"));
        serialColumn.setCellValueFactory(new PropertyValueFactory<Car, String>("serial_number"));

        parkingAutoBrandColumn.setCellValueFactory(new PropertyValueFactory<Car, String>("brand"));

        brandColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        FilteredList<Car> filteredDataMyCars = getCarFilteredList(carsRepo.myCars, filterFieldMyCars);

        FilteredList<Car> filteredData = getCarFilteredList(carsRepo.usersDataActiveCars, filterField);

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
        tableMyCars.setItems(carsRepo.myCars);
        tableMyCars.getItems().removeAll(tableMyCars.getSelectionModel().getSelectedItem());
    }

    @FXML
    private void deactivateParking() {
        tableAllCars.setItems(carsRepo.usersDataActiveCars);
        if(userRepo.usersData.get(0) == tableAllCars.getSelectionModel().getSelectedItem().getUser()) {
            tableAllCars.getItems().removeAll(tableAllCars.getSelectionModel().getSelectedItem());
            tableAllCars.getSelectionModel().getSelectedItem().setActiveParking(false);
        }
    }

    @FXML
    public void handleCreateCar() {
        redirect(CreateCarController.class);
    }


    @FXML
    public void onEditChange(TableColumn.CellEditEvent<Car, String> carStringCellEditEvent) {
        Car car = tableMyCars.getSelectionModel().getSelectedItem();
        car.setBrand(carStringCellEditEvent.getNewValue());
    }
}
