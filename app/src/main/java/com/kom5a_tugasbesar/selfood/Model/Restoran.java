package com.kom5a_tugasbesar.selfood.Model;

import java.util.List;

public class Restoran {

    private String email, name, phoneNumber, type, address;
    private List<Table> tables;

    public Restoran() {
    }

    public Restoran(String email, String name, String phoneNumber, String type, String address, List<Table> tables) {
        this.email = email;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.type = type;
        this.address = address;
        this.tables = tables;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Table> getTables() {
        return tables;
    }

    public void setTables(List<Table> tables) {
        this.tables = tables;
    }
}