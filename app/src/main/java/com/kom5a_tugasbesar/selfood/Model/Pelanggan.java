package com.kom5a_tugasbesar.selfood.Model;

public class Pelanggan {

    private String email, fullName, phoneNumber;

    public Pelanggan() {
    }

    public Pelanggan(String email, String fullName, String phoneNumber) {
        this.email = email;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
