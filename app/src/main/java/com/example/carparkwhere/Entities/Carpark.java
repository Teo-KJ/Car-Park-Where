package com.example.carparkwhere.Entities;

import java.util.ArrayList;

public class Carpark {

    public class CarparkCarDetails {

        public class CarparkPriceJson{
            public String price;
            public String description;
        }

        public class PredictedAvailabilityJson{
            public String time;
            public Integer predictedAvailability;
        }


        public String vehicleType;
        public Integer capacity;
        public Integer liveAvailability;
        public ArrayList<CarparkPriceJson> prices;
        public ArrayList<PredictedAvailabilityJson> predictedAvailabilities;

    }


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


    public String carparkNo;
    public Double latitude;
    public Double longitude;
    public String parkingSystem;
    public Boolean isURANotHDB;
    public String carparkName;
    public CarparkCarDetails carDetails;
    public ArrayList<CarparkPrices> carparkPrices;
    public Integer liveAvailability;

    public Carpark(String carparkNo, Double latitude, Double longitude, String parkingSystem, Boolean isURANotHDB, String carparkName,
                   CarparkCarDetails carDetails){
        this.carparkNo = carparkNo;
        this.latitude = latitude;
        this.longitude = longitude;
        this.parkingSystem = parkingSystem;
        this.isURANotHDB = isURANotHDB;
        this.carparkName = carparkName;
        this.carDetails = carDetails;
    }

    public String getCarparkName() {
        return carparkName;
    }
}
