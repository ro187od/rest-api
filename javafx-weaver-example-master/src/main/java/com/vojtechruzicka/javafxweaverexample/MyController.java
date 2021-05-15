package com.vojtechruzicka.javafxweaverexample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/*
какие окна нужны.
1) окно выбора авторизации - пользователь или админ
2) окно авторизации пользователя
3) окно авторизации админа
4) окно админа (расписал в заметке в тг - там таб с машинами пользщователями и админами)
5) окно пользователя - таб с автостоянкой (табличка со всеми машинами)
 */
@Component
@FxmlView("login.fxml")
public class MyController {

    @FXML
    private Label weatherLabel;
    private WeatherService weatherService;
    private RestTemplate restTemplate;

    @Autowired
    private FxWeaver fxWeaver;
    private Stage stage;

    //сущности JavaFX с которыми нужно разобраться
    //1) Как сделать таблицу в javaFX (TableView - загуглить как добавить в таблицу строчку с помощью FXML)
    //2) TextField - текстовое поле
    //3) Text/Label - просто текст
    //4) Button - кнопка (onAction)
    @Autowired
    public MyController(WeatherService weatherService, RestTemplate restTemplate) {
        this.weatherService = weatherService;
        this.restTemplate = restTemplate;
    }

    public void addCar(ActionEvent actionEvent) {

        Greeting forObject = restTemplate.getForObject(
                "http://localhost:8095/greeting", Greeting.class);

        Parent root = fxWeaver.loadView(MyController.class);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void roleChoise(ActionEvent actionEvent) {
        Greeting forObject = restTemplate.getForObject(
                "http://localhost:8081/greeting", Greeting.class);

        System.out.println(forObject);
        Parent root = fxWeaver.loadView(RegistrationController.class);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }



    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
