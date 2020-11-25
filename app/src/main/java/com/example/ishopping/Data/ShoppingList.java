package com.example.ishopping.Data;

import java.util.ArrayList;
import java.util.Calendar;

public class ShoppingList {

    private ArrayList<Product> productList;
    private String shoppingListDate;
    private String shoppingListCost;
    private String isOpen;

    public ShoppingList() {
        this.productList = new ArrayList<Product>();
        this.shoppingListDate = java.text.DateFormat.getDateInstance().format(Calendar.getInstance().getTime());
        this.shoppingListCost = "";
        this.isOpen="true";
    }


    public ArrayList<Product> getProductList() {
        return productList;
    }

    public void setProductList(ArrayList<Product> productList) {
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
