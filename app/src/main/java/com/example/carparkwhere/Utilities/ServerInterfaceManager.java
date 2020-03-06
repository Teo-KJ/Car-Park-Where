package com.example.carparkwhere.Utilities;
import android.util.Pair;

import com.example.carparkwhere.Models.CarparkJson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ServerInterfaceManager {


    public static void getCarparkDetailsByID(String carparkID,Callback handler){
        OkHttpClient client = new OkHttpClient();
        String url = "http://3.14.70.180:3002/client/carparkdetails/" + carparkID;
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(handler);
    }



    //here is an example of how to call and use this function
//    ServerInterfaceManager.getAllCarparks(new Callback() {
//        @Override
//        public void onFailure(@NotNull Call call, @NotNull IOException e) {
//            e.printStackTrace();
//        }
//
//        @Override
//        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//            Gson gson = new Gson();
//            String responseBody = response.body().string();
//            MapsActivity.this.carparks = gson.fromJson(responseBody, (new TypeToken<ArrayList<CarparkJson>>(){}.getType()));
//
//            MapsActivity.this.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    for (CarparkJson carparkJson:carparks){
//                        LatLng carparkLocation = new LatLng(carparkJson.latitude, carparkJson.longitude);
//                        mMap.addMarker(new MarkerOptions().position(carparkLocation).title(carparkJson.carparkName));
//                    }
//                }
//            });
//
//
//        }
//    });

}
