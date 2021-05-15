package com.vojtechruzicka.javafxweaverexample.controller;

import com.vojtechruzicka.javafxweaverexample.model.Role;
import com.vojtechruzicka.javafxweaverexample.model.User;
import com.vojtechruzicka.javafxweaverexample.repo.UserRepo;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
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

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private FxWeaver fxWeaver;
    private Stage stage;

    public void signup(){

        User user = new User(username.getText(), password.getText(), Role.USER);
        userRepo.usersData.add(user);
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        RestTemplate restTemplate = new RestTemplate();
//
//        // Data attached to the request.
//        HttpEntity<User> requestBody = new HttpEntity<>(user, headers);
//        Boolean result = restTemplate.postForObject(
//                "http://localhost:8081/users/register", requestBody, Boolean.class);
        if(user.getUsername() != null){
            Parent root = fxWeaver.loadView(LoginController.class);
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }
}
