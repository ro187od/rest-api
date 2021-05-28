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
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Executable;

@Component
@FxmlView("registration.fxml")
public class SignUpController extends DefaultJavaFXController {

    protected final static String TITLE = "Регистрация";

    @FXML
    private TextField textFieldLogin;

    @FXML
    private PasswordField textFieldPassword;

    @FXML
    private TextField textFieldMoney;

    @FXML
    public Button buttonSignUp;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private FxWeaver fxWeaver;
    private Stage stage;

    public void signUp() {
        String login = textFieldLogin.getText();
        String password = textFieldPassword.getText();
        Integer money = Integer.valueOf(textFieldMoney.getText());

        try {
            if (!validationService.validateCreateUser(login, password, money)) {
                alertService.showAlert(AlertService.AlertType.PASSWORD_REGEX_WARNING);
            } else {
                User user = new User(login, password, Role.USER, money);
                HttpEntity<User> requestBody = new HttpEntity<>(user, getJsonHttpHeaders());
                Boolean result = restTemplate.postForObject(
                        REST_SERVER_URL + "users/register", requestBody, Boolean.class);

                if (result) {
                    Stage stage = (Stage) buttonSignUp.getScene().getWindow();
                    stage.close();
                }else{
                    alertService.showAlert(AlertService.AlertType.LOGIN_IS_BUSY);
                }
            }
        }catch (Exception e){
            alertService.showAlert(AlertService.AlertType.NO_CONNECTED);
        }

    }
}
