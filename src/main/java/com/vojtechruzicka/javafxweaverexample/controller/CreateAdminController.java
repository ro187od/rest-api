package com.vojtechruzicka.javafxweaverexample.controller;

import com.vojtechruzicka.javafxweaverexample.model.Role;
import com.vojtechruzicka.javafxweaverexample.model.User;
import com.vojtechruzicka.javafxweaverexample.repo.UserRepo;
import com.vojtechruzicka.javafxweaverexample.service.AlertService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;

@Component
@FxmlView("form_admin.fxml")
public class CreateAdminController extends DefaultJavaFXController {

    protected final static String TITLE = "Добавление нового администратора";

    @Autowired
    private UserRepo userRepo;

    @FXML
    private TextField textFieldLogin;

    @FXML
    private PasswordField passwordFieldPassword;

    @FXML
    public void createAdmin() {
        String login = textFieldLogin.getText();
        String password = passwordFieldPassword.getText();

        if (!validationService.validateLoginAndPassword(login, password)) {
            alertService.showAlert(AlertService.AlertType.PASSWORD_REGEX_WARNING);
        } else {
            User admin = new User(login, password, Role.ADMIN);
            HttpEntity<User> createAdminRequestBody = new HttpEntity<>(admin, getJsonHttpHeaders());
            restClient.postForObject(REST_SERVER_URL + "users/register", createAdminRequestBody, Boolean.class);
            userRepo.setAdmins();
            Stage stage = (Stage) textFieldLogin.getScene().getWindow();
            stage.close();
        }
    }
}