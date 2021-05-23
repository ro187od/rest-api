package com.vojtechruzicka.javafxweaverexample.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.List;

@NoArgsConstructor
@Data
public class User {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("username")
    private String username;
    @JsonProperty("password")
    private String password;
    @JsonProperty("role")
    private Role role;
    @JsonProperty("numberMachines")
    private Integer numberMachines;
    @JsonProperty("moneyInAccount")
    private Integer moneyInAccount;

    public User(String username, String password, Role role, Integer moneyInAccount) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.moneyInAccount = moneyInAccount;
    }

    public User(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }
}
