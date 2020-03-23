package com.example.carparkwhere.ModelObjects;

public class BookmarkedCarparks {
    private String carpark_id;
    private boolean expanded;

    public BookmarkedCarparks(String carpark_id){
        this.carpark_id = carpark_id;
        this.expanded = false;
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
