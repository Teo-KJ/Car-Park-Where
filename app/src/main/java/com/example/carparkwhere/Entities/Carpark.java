package com.example.carparkwhere.Entities;

import java.util.ArrayList;

/**
 * The Carpark entity class.
 * @author Teo Kai Jie
 * @version 1.0
 */

public class Carpark {

    /*
     * Constructor for details of a single carpark
     */
    public class CarparkCarDetails {

        /*
         * Price and description of a single carpark, originally in JSON as it was taken from the API.
         */
        public class CarparkPriceJson{
            public String price;
            public String description;
        }

        /**
         * Predicted availability of a single carpark
         */
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

    /*
     * Constructor for price and description of a single carpark
     */
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

    /*
     * Constructor for the complete carpark information and availability
     */
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