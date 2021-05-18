package com.vojtechruzicka.javafxweaverexample.controller;

import com.vojtechruzicka.javafxweaverexample.model.Role;
import com.vojtechruzicka.javafxweaverexample.model.User;
import com.vojtechruzicka.javafxweaverexample.repo.UserRepo;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@FxmlView("registration.fxml")
public class RegistrationController extends DefaultJavaFXController {

    @FXML
    private TextField username;

    @FXML
    private TextField password;

    @FXML
    public Button saveButtom;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private FxWeaver fxWeaver;
    private Stage stage;

    public void signup(){
        if(username.getText().length() != 0 || password.getText().length() != 0) {
            User user = new User(username.getText(), password.getText(), Role.USER);
            HttpEntity<User> requestBody = new HttpEntity<>(user, getHttpHeaders());
            Boolean result = restTemplate.postForObject(
                    "http://localhost:8082/users/register", requestBody, Boolean.class);

            if (result == true) {
                Stage stage = (Stage) saveButtom.getScene().getWindow();
                stage.close();
            }else{
                System.out.println("Пользователь с таким логином уже существует");
            }
        }else{
            System.out.println("Вы не ввели пароль или логин");
        }
    }

    private HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

}
