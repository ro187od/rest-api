package com.vojtechruzicka.javafxweaverexample.controller;

import com.vojtechruzicka.javafxweaverexample.model.Car;
import com.vojtechruzicka.javafxweaverexample.model.Role;
import com.vojtechruzicka.javafxweaverexample.model.User;
import com.vojtechruzicka.javafxweaverexample.repo.CarsRepo;
import com.vojtechruzicka.javafxweaverexample.repo.UserRepo;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
@FxmlView("form_user.fxml")
public class CreateUserController extends DefaultJavaFXController{

    @Autowired
    private UserRepo userRepo;

    @FXML
    private TextField username;

    @FXML
    private TextField password;

    @FXML
    public Button saveButtom;


    @FXML
    public void save(){
        User user = new User(username.getText(), password.getText(), Role.USER);
        HttpEntity<User> requestBody = new HttpEntity<>(user, getHttpHeaders());
        Boolean result = restTemplate.postForObject(
                "http://localhost:8082/users/register", requestBody, Boolean.class);
        userRepo.setUsers();
        Stage stage = (Stage) saveButtom.getScene().getWindow();
        stage.close();
    }

    private HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}
