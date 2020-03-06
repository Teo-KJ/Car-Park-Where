package com.example.carparkwhere.Utilities;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class ServerInterfaceManager {

    public static void getAllCarparks(Callback handler){
        OkHttpClient client = new OkHttpClient();
        String url = "http://3.14.70.180:3002/client/carparkdetails";
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
//            MapsActivity.this.carparks = gson.fromJson(responseBody, (new TypeToken<ArrayList<Carpark>>(){}.getType()));
//
//            MapsActivity.this.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    for (Carpark carpark:carparks){
//                        LatLng carparkLocation = new LatLng(carpark.latitude, carpark.longitude);
//                        mMap.addMarker(new MarkerOptions().position(carparkLocation).title(carpark.carparkName));
//                    }
//                }
//            });
//
//
//        }
//    });

}
