package com.example.carparkwhere.Models;

public class CarparkJson {

    public String carparkNo;
    public Double latitude;
    public Double longitude;
    public String parkingSystem;
    public Integer capacity;
    public String vehicleType;
    public Boolean isURANotHDB;
    public String carparkName;
    public Integer liveAvailability;

    public CarparkJson(String carparkNo, Double latitude, Double longitude, String parkingSystem, Integer capacity, String vehicleType, Boolean isURANotHDB, String carparkName,Integer liveAvailability){
        this.carparkNo = carparkNo;
        this.latitude = latitude;
        this.longitude = longitude;
        this.parkingSystem = parkingSystem;
        this.capacity = capacity;
        this.vehicleType = vehicleType;
        this.isURANotHDB = isURANotHDB;
        this.carparkName = carparkName;
        this.liveAvailability = liveAvailability;
    }
}
