package com.example.carparkwhere.ModelObjects;

public class CarparkPrices {

    public String description;
    public String price;

    public CarparkPrices(String description, String price){
        this.description = description;
        this.price = price;
    }

    public String getDescription(){
        return description;
    }

    public String getPrice() {
        return price;
    }
}
