package com.kom5a_tugasbesar.selfood.Model;

public class Table {

    private int number;
    private String pelanggan_id, status;

    public Table() {
    }

    public Table(int number, String pelanggan_id, String status) {
        this.number = number;
        this.pelanggan_id = pelanggan_id;
        this.status = status;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getPelanggan_id() {
        return pelanggan_id;
    }

    public void setPelanggan_id(String pelanggan_id) {
        this.pelanggan_id = pelanggan_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
