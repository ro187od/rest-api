package com.vojtechruzicka.javafxweaverexample.controller;

import com.vojtechruzicka.javafxweaverexample.model.Role;
import com.vojtechruzicka.javafxweaverexample.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@FxmlView("login.fxml")
public class LoginController extends DefaultJavaFXController {

    @FXML
    private TextField username;

    @FXML
    private TextField password;

    public void login() {
        //здесь должен быть валидатор ввода логина и пароля
        //alertService + validationService
        User user = new User(username.getText(), password.getText(), Role.USER);

        // Data attached to the request.
        HttpEntity<User> requestBody = new HttpEntity<>(user, getHttpHeaders());

//        User result = restTemplate.postForObject(
//                "http://localhost:8081/login", requestBody, User.class);
//
//        System.out.println(result);
        User result = new User();
        if (result != null){
            showCurrentStageWindow(UserPageController.class, UserPageController.TITLE);
        }
    }

    private HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    public void signup(ActionEvent actionEvent) {
        Parent root = fxWeaver.loadView(RegistrationController.class);
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}