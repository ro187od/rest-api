package com.vojtechruzicka.javafxweaverexample.model;


public class Car {

    private int id;
    private String brand;
    private String serial_number;
    private User user;
    private boolean activeParking;

    public Car(int id, String brand, String serial_number, User user) {
        this.activeParking = true;
        this.user = user;
        this.id = id;
        this.brand = brand;
        this.serial_number = serial_number;
    }

    public User getUser() {
        return user;
    }

    public boolean isActiveParking() {
        return activeParking;
    }

    public void setActiveParking(boolean activeParking) {
        this.activeParking = activeParking;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getSerial_number() {
        return serial_number;
    }

    public void setSerial_number(String serial_number) {
        this.serial_number = serial_number;
    }
}