package com.vojtechruzicka.javafxweaverexample.controller;

import com.vojtechruzicka.javafxweaverexample.model.Role;
import com.vojtechruzicka.javafxweaverexample.model.User;
import com.vojtechruzicka.javafxweaverexample.repo.UserRepo;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@FxmlView("form_admin.fxml")
public class CreateAdminController extends DefaultJavaFXController{

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
        User user = new User(username.getText(), password.getText(), Role.ADMIN);
        userRepo.usersData.add(user);
        userRepo.sortRole();
        Stage stage = (Stage) saveButtom.getScene().getWindow();
        stage.close();
    }

}
