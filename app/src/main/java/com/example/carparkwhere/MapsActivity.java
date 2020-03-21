package com.example.carparkwhere;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.example.carparkwhere.Models.Carpark;
import com.example.carparkwhere.Utilities.NetworkCallEventListener;
import com.example.carparkwhere.Utilities.ServerInterfaceManager;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    Location currentLocation;
    private GoogleMap googleMap;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;
    ArrayList<Carpark> carparks = new ArrayList<Carpark>();
    int height = 40;
    int width = 40;
    Bitmap greenParking;
    Bitmap yellowParking;
    Bitmap redParking;
    Bitmap blackParking;
    Bitmap greyParking;
    PlacesClient placesClient;
    String apiKey = "AIzaSyDl-riH0Iuqpm4dzMdEvGy_a6M1psWJOrs";
    Map<String, String> mMarkerMap = new HashMap<>();
    Map<String, Double> mDistanceMap = new HashMap<>();
    Map<String, Double> sortedDistanceMap = new HashMap<>();
    Marker currentMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Setup Places Client
        if (!Places.isInitialized()) {
            Places.initialize(MapsActivity.this, apiKey);
        }
// Retrieve a PlacesClient (previously initialized - see MainActivity)
        placesClient = Places.createClient(this);

        final AutocompleteSupportFragment autocompleteSupportFragment =
                (AutocompleteSupportFragment)
                        getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        autocompleteSupportFragment.setCountry("SG");

        autocompleteSupportFragment.setPlaceFields(Arrays.asList(Place.Field.ID,  Place.Field.NAME,Place.Field.LAT_LNG,Place.Field.ADDRESS));
        autocompleteSupportFragment.setCountry("SG");
        autocompleteSupportFragment.setOnPlaceSelectedListener(
                new PlaceSelectionListener() {
                    @Override
                    public void onPlaceSelected(Place place) {
                        LatLng latLng = place.getLatLng();
                        if(currentMarker!=null)
                        {
                            currentMarker.remove();
                        }

                        MarkerOptions markerOption= new MarkerOptions();
                        markerOption.position(latLng);
                        markerOption.title(place.getName());
                        currentMarker= googleMap.addMarker(markerOption);
                        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

                        Toast.makeText(MapsActivity.this, ""+latLng.latitude, Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onError(Status status) {
                        Toast.makeText(MapsActivity.this, ""+status.getStatusMessage(), Toast.LENGTH_SHORT).show();
                    }
                });




        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLocation();


    }


    private void makeBitmap() {
        BitmapDrawable g = (BitmapDrawable)getResources().getDrawable(R.drawable.green_car);
        BitmapDrawable y = (BitmapDrawable)getResources().getDrawable(R.drawable.yellow_car);
        BitmapDrawable r = (BitmapDrawable)getResources().getDrawable(R.drawable.red_car);
        BitmapDrawable b = (BitmapDrawable)getResources().getDrawable(R.drawable.black_car);
        BitmapDrawable gr = (BitmapDrawable)getResources().getDrawable(R.drawable.grey_car);
        Bitmap b1 = g.getBitmap();
        Bitmap b2 = y.getBitmap();
        Bitmap b3 = r.getBitmap();
        Bitmap b4 = b.getBitmap();
        Bitmap b5 = gr.getBitmap();
        greenParking = Bitmap.createScaledBitmap(b1, width, height, false);
        yellowParking = Bitmap.createScaledBitmap(b2, width, height, false);
        redParking = Bitmap.createScaledBitmap(b3, width, height, false);
        blackParking = Bitmap.createScaledBitmap(b4, width, height, false);
        greyParking = Bitmap.createScaledBitmap(b5, width, height, false);

    }

    private void fetchLocation() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                    Toast.makeText(getApplicationContext(), currentLocation.getLatitude() + "" + currentLocation.getLongitude(), Toast.LENGTH_SHORT).show();
                    SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    assert supportMapFragment != null;
                    supportMapFragment.getMapAsync(MapsActivity.this);

                }
            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onMapReady(GoogleMap googleMaps) {
       googleMap=googleMaps;
        makeBitmap();
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        ServerInterfaceManager.getAllCarparkEntireFullDetails(this, new NetworkCallEventListener() {
            @Override
            public <T> void onComplete(T networkCallResult, Boolean isSuccessful, String errorMessage) {
                if (isSuccessful) {
                    carparks = (ArrayList<Carpark>) networkCallResult;
                    int fullness;
                    int availability=100;
                    int capacity;
                    int total=300;
                    double distance;
                    Location mark=new Location ("");
                    for (int counter = 0; counter < carparks.size(); counter++) {
                        mark.setLatitude(carparks.get(counter).latitude);
                        mark.setLongitude(carparks.get(counter).longitude);
                        distance= currentLocation.distanceTo(mark);
                        if(carparks.get(counter).carDetails.liveAvailability!=null && carparks.get(counter).carDetails.capacity!=null )
                        {
                            fullness = carparks.get(counter).carDetails.liveAvailability/ carparks.get(counter).carDetails.capacity;
                            if(fullness>=0.7 && fullness<=1)
                            {
                                Marker marker =googleMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(mark.getLatitude(), mark.getLongitude()))
                                        .icon(BitmapDescriptorFactory.fromBitmap(greenParking)));
                                mMarkerMap.put(marker.getId(), carparks.get(counter).carparkNo);
                            }
                            else if(fullness>=0.4 && fullness<0.7)
                            {
                                Marker marker =googleMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(mark.getLatitude(), mark.getLongitude()))
                                        .icon(BitmapDescriptorFactory.fromBitmap(yellowParking)));
                                mMarkerMap.put(marker.getId(), carparks.get(counter).carparkNo);
                            }
                            else if(fullness>0&& fullness<0.4)
                            {
                                Marker marker =googleMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(mark.getLatitude(), mark.getLongitude()))
                                        .icon(BitmapDescriptorFactory.fromBitmap(redParking)));
                                mMarkerMap.put(marker.getId(), carparks.get(counter).carparkNo);
                            }
                            else
                            {
                                Marker marker =googleMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(mark.getLatitude(), mark.getLongitude()))
                                        .icon(BitmapDescriptorFactory.fromBitmap(greyParking)));
                                mMarkerMap.put(marker.getId(), carparks.get(counter).carparkNo);
                            }
                        }
                        else
                        {
                            Marker marker =googleMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(mark.getLatitude(), mark.getLongitude()))
                                    .icon(BitmapDescriptorFactory.fromBitmap(blackParking)));
                            mMarkerMap.put(marker.getId(), carparks.get(counter).carparkNo);
                        }
                        mDistanceMap.put(carparks.get(counter).carparkNo, distance);
                    }

                }else{
                    //deal with the error message, maybe toast or something
                }
            }
        });
        sortedDistanceMap=mDistanceMap.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("Current Location");

        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        googleMap.addMarker(markerOptions);

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                String carparkID = mMarkerMap.get(marker.getId());
                LatLng latLng = marker.getPosition();
                double longnitude =  latLng.longitude;
                double latitude =  latLng.latitude;
                Intent intent = new Intent(MapsActivity.this, DetailCarparkActivity.class);
                intent.putExtra("CARPARK_ID", carparkID);
                intent.putExtra("Lat", latitude);
                intent.putExtra("Lng", longnitude);
                startActivity(intent);

                return false;
            }
        });

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchLocation();
                }
                break;
        }
    }

    // Function to open up the dialogue reminding user to switch on location
    public void openDialog() {
        Dialog help = new Dialog(MapsActivity.this);
        help.requestWindowFeature(Window.FEATURE_NO_TITLE);
        help.setContentView(R.layout.switch_on_location);
        help.show();
    }
}
