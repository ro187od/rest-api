package com.vojtechruzicka.javafxweaverexample.controller;

import com.vojtechruzicka.javafxweaverexample.model.Role;
import com.vojtechruzicka.javafxweaverexample.model.User;
import com.vojtechruzicka.javafxweaverexample.model.UserStatus;
import com.vojtechruzicka.javafxweaverexample.service.AlertService;
import com.vojtechruzicka.javafxweaverexample.service.ValidationService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;

@Component
@FxmlView("login.fxml")
@RequiredArgsConstructor
public class LoginController extends DefaultJavaFXController {

    protected final static String TITLE = "Вход";

    @FXML
    private TextField textFieldLogin;

    @FXML
    private TextField textFieldPassword;

    private final ValidationService validationService;
    private final AlertService alertService;

    public void login() {
        String login = textFieldLogin.getText();
        String password = textFieldPassword.getText();

        if (!validationService.validateEmptyLines(login, password)) {
            alertService.showAlert(AlertService.AlertType.PASSWORD_OR_LOGIN_IS_EMPTY);
        } else {
            User user = new User(login, password, Role.USER);
            HttpEntity<User> requestBody = new HttpEntity<>(user, getJsonHttpHeaders());
            UserStatus result = restClient.postForObject(REST_SERVER_URL + "/login", requestBody, UserStatus.class);

            if (UserStatus.UNKNOWER.equals(result)) {
                alertService.showAlert(AlertService.AlertType.USER_NOT_FOUND);
            } else if (UserStatus.CREATED_ADMIN.equals(result)) {
                showCurrentStageWindow (AdminPageController.class, AdminPageController.TITLE);
            } else if (UserStatus.CREATED_USER.equals(result)) {
                showCurrentStageWindow (UserPageController.class, UserPageController.TITLE);
            }
        }
    }

    public void signup(ActionEvent actionEvent) {
        Parent root = fxWeaver.loadView(SignUpController.class);
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
