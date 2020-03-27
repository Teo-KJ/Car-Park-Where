package com.example.carparkwhere.ModelObjects;

public class BookmarkedCarpark {
    private String carpark_id;
    private boolean expanded;
    private String carparkName;
    private String availability;

    public BookmarkedCarpark(String carpark_id){
        this.carpark_id = carpark_id;
        this.expanded = false;
        this.carparkName = "";
        this.availability = "";

    }

    public void setCarparkName(String carparkName) {
        this.carparkName = carparkName;
    }

    public String getCarparkName() {
        return carparkName;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getAvailability() {
        return availability;
    }

    public String getCarparkID(){
        return carpark_id;
    }
    public boolean isExpanded(){
        return expanded;
    }
    public void setExpanded(boolean expanded){
        this.expanded = expanded;
    }
}