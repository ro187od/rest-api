package com.vojtechruzicka.javafxweaverexample;

import javafx.event.ActionEvent;
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
@FxmlView("login.fxml")
public class LoginController {

    @FXML
    private TextField username;

    @FXML
    private TextField password;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private FxWeaver fxWeaver;
    private Stage stage;

    public void signin() {
        User user = new User(username.getText(), password.getText(), Role.USER);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);

        RestTemplate restTemplate = new RestTemplate();

        // Data attached to the request.
        HttpEntity<User> requestBody = new HttpEntity<>(user, headers);

        User result = restTemplate.postForObject(
                "http://localhost:8081/login", requestBody, User.class);

        System.out.println(result);
        if(result != null){
            Parent root = fxWeaver.loadView(UserPageController.class);
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }

    public void signup(ActionEvent actionEvent) {
        Parent root = fxWeaver.loadView(RegistrationController.class);
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
