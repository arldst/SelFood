package com.kom5a_tugasbesar.selfood.Model;

public class Table {

    private String pelanggan_id;
    private int status;

    public Table() {
    }

    public Table(String pelanggan_id, int status) {
        this.pelanggan_id = pelanggan_id;
        this.status = status;
    }

    public String getPelanggan_id() {
        return pelanggan_id;
    }

    public void setPelanggan_id(String pelanggan_id) {
        this.pelanggan_id = pelanggan_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
