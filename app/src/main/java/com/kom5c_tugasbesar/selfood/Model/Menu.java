package com.kom5c_tugasbesar.selfood.Model;

public class Menu {

    private String name, description, foodImgUrl;
    private int price, itemCount;

    public Menu() {
    }

    public Menu(String name, String description, String foodImgUrl, int price) {
        this.name = name;
        this.description = description;
        this.foodImgUrl = foodImgUrl;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFoodImgUrl() {
        return foodImgUrl;
    }

    public void setFoodImgUrl(String foodImgUrl) {
        this.foodImgUrl = foodImgUrl;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }
}
