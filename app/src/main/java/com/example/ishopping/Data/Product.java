package com.example.ishopping.Data;

import android.media.Image;

public class Product {

    private String productName;
    private String productQuantity;
    private String productCategory;
    private String productDescription;
    private String productImage;

    public Product(){

    }

    public Product(String productName, String productQuantity, String productCategory, String productDescription, String productImage) {
        this.productName = productName;
        this.productQuantity = productQuantity;
        this.productCategory = productCategory;
        this.productDescription = productDescription;
        this.productImage = productImage;
    }


    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(String productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }
}
