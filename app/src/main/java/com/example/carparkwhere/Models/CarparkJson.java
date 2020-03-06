package com.example.carparkwhere.Models;

<<<<<<< Updated upstream
public class CarparkJson {

=======
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

>>>>>>> Stashed changes
    public String carparkNo;
    public Double latitude;
    public Double longitude;
    public String parkingSystem;
<<<<<<< Updated upstream
    public Integer capacity;
    public String vehicleType;
    public Boolean isURANotHDB;
    public String carparkName;
    public Integer liveAvailability;

    public CarparkJson(String carparkNo, Double latitude, Double longitude, String parkingSystem, Integer capacity, String vehicleType, Boolean isURANotHDB, String carparkName,Integer liveAvailability){
=======
    public Boolean isURANotHDB;
    public String carparkName;
    public CarparkCarDetailsJson carDetails;

    public CarparkJson(String carparkNo, Double latitude, Double longitude, String parkingSystem, Boolean isURANotHDB, String carparkName, CarparkCarDetailsJson carDetails){
>>>>>>> Stashed changes
        this.carparkNo = carparkNo;
        this.latitude = latitude;
        this.longitude = longitude;
        this.parkingSystem = parkingSystem;
<<<<<<< Updated upstream
        this.capacity = capacity;
        this.vehicleType = vehicleType;
        this.isURANotHDB = isURANotHDB;
        this.carparkName = carparkName;
        this.liveAvailability = liveAvailability;
=======
        this.isURANotHDB = isURANotHDB;
        this.carparkName = carparkName;
        this.carDetails = carDetails;
>>>>>>> Stashed changes
    }
}
