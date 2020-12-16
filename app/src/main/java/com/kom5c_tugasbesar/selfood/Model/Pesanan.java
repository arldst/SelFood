package com.kom5c_tugasbesar.selfood.Model;

import java.util.List;

public class Pesanan {

    private String name, status;
    private int tableNum;
    private List<Menu> menus;

    public Pesanan() {
    }

    public Pesanan(String name, String status, int tableNum) {
        this.name = name;
        this.status = status;
        this.tableNum = tableNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTableNum() {
        return tableNum;
    }

    public void setTableNum(int tableNum) {
        this.tableNum = tableNum;
    }

    public List<Menu> getMenus() {
        return menus;
    }

    public void setMenus(List<Menu> menus) {
        this.menus = menus;
    }
}
