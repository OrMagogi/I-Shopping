package com.example.ishopping.Data;

import java.util.ArrayList;
import java.util.Calendar;

public class ShoppingList {

    private String shoppingListId;
    private ArrayList<Product> productList;
    private String shoppingListDate;
    private String shoppingListCost;

    public ShoppingList() {
        this.productList = new ArrayList<Product>();
        this.shoppingListDate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
        this.shoppingListCost = "";
    }

    public String getShoppingListId() {
        return shoppingListId;
    }

    public void setShoppingListId(String shoppingListId) {
        this.shoppingListId = shoppingListId;
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
}
