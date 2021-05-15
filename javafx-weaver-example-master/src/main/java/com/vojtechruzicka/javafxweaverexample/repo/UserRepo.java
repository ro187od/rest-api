package com.vojtechruzicka.javafxweaverexample.repo;

import com.vojtechruzicka.javafxweaverexample.model.Role;
import com.vojtechruzicka.javafxweaverexample.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.stereotype.Component;

@Component
public class UserRepo {
    public ObservableList<User> usersData = FXCollections.observableArrayList();
    public ObservableList<User> roleAdminData = FXCollections.observableArrayList();
    public ObservableList<User> roleUserData = FXCollections.observableArrayList();

    public void initData() {
        User user = new User("user", "123", Role.USER);
        User admin = new User("admin", "123", Role.ADMIN);
        usersData.add(user);
        usersData.add(admin);
        sortRole();
    }

    public void sortRole(){
        roleUserData.clear();
        roleAdminData.clear();
        usersData.forEach(user -> {
            if (user.getRole() == Role.ADMIN){
                roleAdminData.add(user);
            }else{
                roleUserData.add(user);
            }
        });
    }
}