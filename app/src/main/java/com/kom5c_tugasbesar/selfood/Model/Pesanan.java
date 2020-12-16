package com.kom5c_tugasbesar.selfood.Model;

import java.util.List;

public class Pesanan {

    private String restoId, name, status;
    private int tableNum;
    private List<Menu> menus;

    public Pesanan() {
    }

    public Pesanan(String restoId, String name, String status, int tableNum) {
        this.restoId = restoId;
        this.name = name;
        this.status = status;
        this.tableNum = tableNum;
    }

    public String getRestoId() {
        return restoId;
    }

    public void setRestoId(String restoId) {
        this.restoId = restoId;
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
