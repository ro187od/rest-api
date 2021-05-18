package com.vojtechruzicka.javafxweaverexample.controller;

import com.vojtechruzicka.javafxweaverexample.model.Car;
import com.vojtechruzicka.javafxweaverexample.model.User;
import com.vojtechruzicka.javafxweaverexample.repo.CarsRepo;
import com.vojtechruzicka.javafxweaverexample.repo.UserRepo;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

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
    private TableColumn<Car, String> brandColumn;

    @FXML
    private TableColumn<Car, String> parkingAutoBrandColumn;

    @FXML
    private TableColumn<Car, String> ownerUsername;

    @FXML
    private TableColumn<Car, String> scope;




    @FXML
    private void initialize() {
        carsRepo.init();
        carsRepo.init();
        ObservableList<Car> myCars = carsRepo.getMyCars();
        ObservableList<Car> carsInParkong = carsRepo.getCarsDataActiveCars();
        idColumn.setCellValueFactory(new PropertyValueFactory<Car, Integer>("id"));
        brandColumn.setCellValueFactory(new PropertyValueFactory<Car, String>("brand"));

        parkingAutoBrandColumn.setCellValueFactory(new PropertyValueFactory<Car, String>("brand"));
        scope.setCellValueFactory(new PropertyValueFactory<Car, String>("scope"));

        ownerUsername.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Car, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Car, String> param) {
                return new SimpleStringProperty(param.getValue().getOwner().getPassword());
            }
        });

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
        Map< String, String > params = new HashMap<String,String>();
        params.put("id", String.valueOf(tableMyCars.getSelectionModel().getSelectedItem().getId()));
        String.valueOf(tableAllCars.getSelectionModel().getSelectedItem());
        restTemplate.delete("http://localhost:8082/car/{id}", params);
        carsRepo.init();
    }

    @FXML
    private void deactivateParking() {
        Car car = tableAllCars.getSelectionModel().getSelectedItem();
        EvaluationController.setCar(car);
        redirect(EvaluationController.class);
    }

    @FXML
    private void activateParking() {
        Car car = tableMyCars.getSelectionModel().getSelectedItem();
        Map<String, String> params=  new HashMap<String, String>();
        params.put("id", String.valueOf(tableMyCars.getSelectionModel().getSelectedItem().getId()));
        restTemplate.put("http://localhost:8082/car/activate/{id}", car, params);
        carsRepo.init();
    }

    @FXML
    private void arithmeticMean() {
        redirect(ArithmeticMeanController.class);
    }

    @FXML
    public void handleCreateCar() {
        redirect(CreateCarController.class);
    }


    @FXML
    public void onEditChange(TableColumn.CellEditEvent<Car, String> carStringCellEditEvent) {
        Car car = tableMyCars.getSelectionModel().getSelectedItem();
        car.setBrand(carStringCellEditEvent.getNewValue());
        Map<String, String> params=  new HashMap<String, String>();
        params.put("id", String.valueOf(car.getId()));
        restTemplate.put("http://localhost:8082/car/update/{id}", car, params);
    }
}
