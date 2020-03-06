package com.example.carparkwhere.Models;

import java.util.ArrayList;

public class Carpark {

    public String carparkNo;
    public Double latitude;
    public Double longitude;
    public String parkingSystem;
    public Integer capacity;
    public String vehicleType;
    public Boolean isURANotHDB;
    public String carparkName;
    public ArrayList<CarparkPrices> carparkPrices;
    public Integer liveAvailability;

    public Carpark(String carparkNo, Double latitude, Double longitude, String parkingSystem, Integer capacity,
                   String vehicleType, Boolean isURANotHDB, String carparkName, ArrayList<CarparkPrices> carparkPrices,
                   Integer liveAvailability){
        this.carparkNo = carparkNo;
        this.latitude = latitude;
        this.longitude = longitude;
        this.parkingSystem = parkingSystem;
        this.capacity = capacity;
        this.vehicleType = vehicleType;
        this.isURANotHDB = isURANotHDB;
        this.carparkName = carparkName;
        this.carparkPrices = carparkPrices;
        this.liveAvailability = liveAvailability;
    }

    public String getCarparkNo() {
        return carparkNo;
    }

    public String getCarparkName() {
        return carparkName;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public Integer getLiveAvailability() {
        return liveAvailability;
    }

    public ArrayList<CarparkPrices> getCarparkPrices() {
        return carparkPrices;
    }
}
