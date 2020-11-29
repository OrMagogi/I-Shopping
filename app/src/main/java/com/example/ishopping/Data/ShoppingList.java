package com.example.ishopping.Data;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class ShoppingList {

    private HashMap<String,Product> productList;
    private String shoppingListDate;
    private String shoppingListCost;
    private String isOpen;

    public ShoppingList() {
        this.productList = new HashMap<String,Product>();
        this.shoppingListDate = java.text.DateFormat.getDateInstance().format(Calendar.getInstance().getTime());
        this.shoppingListCost = "";
        this.isOpen="true";
    }

    public HashMap<String, Product> getProductList() {
        return productList;
    }

    public void setProductList(HashMap<String, Product> productList) {
        this.productList = productList;
    }

    public String getDate() {
        return shoppingListDate;
    }

    public void setDate(String date) {
        this.shoppingListDate = date;
    }

    public String getCost() {
        return shoppingListCost;
    }

    public void setCost(String cost) {
        this.shoppingListCost = cost;
    }

    public String getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(String isOpen) {
        this.isOpen = isOpen;
    }
}
