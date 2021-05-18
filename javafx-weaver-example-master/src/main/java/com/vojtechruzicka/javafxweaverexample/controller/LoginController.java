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

@Component
@FxmlView("login.fxml")
public class LoginController extends DefaultJavaFXController {

    @FXML
    private TextField username;

    @FXML
    private TextField password;

    public void login() {
        if(username.getText().length() != 0 || password.getText().length() != 0){
            User user = new User(username.getText(), password.getText(), Role.USER);
            HttpEntity<User> requestBody = new HttpEntity<>(user, getHttpHeaders());
            User result = restTemplate.postForObject(
                    "http://localhost:8082/users/login", requestBody, User.class);
            if (result == null){
                System.out.println("Пользователя нет в базе данных");
                return;
            }else if (result.getRole() == Role.ADMIN){
                System.out.println(result.getUsername());
                showCurrentStageWindow(AdminPageController.class, UserPageController.TITLE);
            }else if (result.getRole() == Role.USER){
                System.out.println(result.getUsername());
                showCurrentStageWindow(UserPageController.class, UserPageController.TITLE);
            }
        }
        System.out.println("Неправильный логин или пароль");
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
