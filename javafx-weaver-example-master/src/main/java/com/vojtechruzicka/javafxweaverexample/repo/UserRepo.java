package com.vojtechruzicka.javafxweaverexample.repo;

import com.vojtechruzicka.javafxweaverexample.controller.DefaultJavaFXController;
import com.vojtechruzicka.javafxweaverexample.model.Role;
import com.vojtechruzicka.javafxweaverexample.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.core.DecoratingClassLoader;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserRepo extends DefaultJavaFXController {

    private ObservableList<User> roleAdminData = FXCollections.observableArrayList();
    private ObservableList<User> roleUserData = FXCollections.observableArrayList();
    private ResponseEntity<List<User>> userData;

    public ObservableList<User> getRoleAdminData() {
        return roleAdminData;
    }

    public ObservableList<User> getRoleUserData() {
        return roleUserData;
    }

    public void init(){
        setAdmins();
        setUsers();
    }
    public void setUsers() {
        roleUserData.clear();
        userData =
                restTemplate.exchange("http://localhost:8082/users",
                        HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<User>>() {
                        });
        userData.getBody().forEach(user -> {
                roleUserData.add(user);
        });
    }

    public void setAdmins() {
        roleAdminData.clear();
        userData =
                restTemplate.exchange("http://localhost:8082/admins",
                        HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<User>>() {
                        });
        userData.getBody().forEach(user -> {
                roleAdminData.add(user);
        });
    }

}