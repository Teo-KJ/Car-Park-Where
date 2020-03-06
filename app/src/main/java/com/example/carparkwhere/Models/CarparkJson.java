package com.example.carparkwhere.Models;

import java.util.ArrayList;

public class CarparkJson {

    public class CarparkCarDetailsJson{

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
        public ArrayList<CarparkPriceJson> carparkPrices;
        public ArrayList<PredictedAvailabilityJson> predictedAvailabilities;

    }

    public String carparkNo;
    public Double latitude;
    public Double longitude;
    public String parkingSystem;
    public Boolean isURANotHDB;
    public String carparkName;
    public CarparkCarDetailsJson carDetails;




    public CarparkJson(String carparkNo, Double latitude, Double longitude, String parkingSystem, Boolean isURANotHDB, String carparkName, CarparkCarDetailsJson carDetails){

        this.carparkNo = carparkNo;
        this.latitude = latitude;
        this.longitude = longitude;
        this.parkingSystem = parkingSystem;
        this.isURANotHDB = isURANotHDB;
        this.carparkName = carparkName;
        this.carDetails = carDetails;
    }
}
