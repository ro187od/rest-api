package com.vojtechruzicka.javafxweaverexample.controller;

import com.vojtechruzicka.javafxweaverexample.model.Role;
import com.vojtechruzicka.javafxweaverexample.model.User;
import com.vojtechruzicka.javafxweaverexample.repo.UserRepo;
import com.vojtechruzicka.javafxweaverexample.service.AlertService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;

@Component
@FxmlView("form_user.fxml")
public class CreateUserController extends DefaultJavaFXController{

    protected final static String TITLE = "Добавление нового пользователя";

    @Autowired
    private UserRepo userRepo;

    @FXML
    private TextField textFieldUsername;

    @FXML
    private TextField textFieldMoney;

    @FXML
    private TextField textFieldPassword;

    @FXML
    public Button saveButtomUser;


    @FXML
    public void save(){
        String login = textFieldUsername.getText();
        String password = textFieldPassword.getText();
        Integer money = Integer.valueOf(textFieldMoney.getText());

        if (!validationService.validateCreateUser(login, password, money)){
            alertService.showAlert(AlertService.AlertType.PASSWORD_REGEX_WARNING);
        }else{
            User user = new User(login, password, Role.USER, money);
            HttpEntity<User> requestBody = new HttpEntity<>(user, getJsonHttpHeaders());
            Boolean result = restClient.postForObject(
                    REST_SERVER_URL + "users/register", requestBody, Boolean.class);
            userRepo.setUsers();
            Stage stage = (Stage) saveButtomUser.getScene().getWindow();
            stage.close();
        }
    }
}
