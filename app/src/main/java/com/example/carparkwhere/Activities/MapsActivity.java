package com.example.carparkwhere.Activities;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.carparkwhere.DAO.DAOImplementations.CarparkDaoImpl;
import com.example.carparkwhere.DAO.DAOImplementations.UserDataDaoFirebaseImpl;
import com.example.carparkwhere.DAO.DAOInterfaces.CarparkDao;
import com.example.carparkwhere.DAO.DAOInterfaces.UserDataDao;
import com.example.carparkwhere.Adaptors.ListMapAdapter;
import com.example.carparkwhere.Entities.BookmarkedCarpark;
import com.example.carparkwhere.Entities.Carpark;
import com.example.carparkwhere.Interfaces.NetworkCallEventListener;
import com.example.carparkwhere.R;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, ListMapAdapter.UserListRecyclerClickListener {
    //Variable that stores current location
    private Location currentLocation;
    //Variable that stores instance of Google Map in this class
    private GoogleMap googleMap;
    //Variable that stores fused location provider client
    private FusedLocationProviderClient fusedLocationProviderClient;
    //Variable that stores request code value
    private static final int REQUEST_CODE = 101;
    //Arraylist that stores carpark variables
    private ArrayList<Carpark> carparks = new ArrayList<Carpark>();
    //Variables that stores bitmap dimension
    private int height = 40;
    private int width = 40;
    //Variables that store bitmaps of carpark icons
    private Bitmap greenParking, yellowParking, redParking, blackParking, greyParking;
    //Client that exposes the Places API
    private PlacesClient placesClient;
    //API key of Maps API
    private String apiKey = "AIzaSyDl-riH0Iuqpm4dzMdEvGy_a6M1psWJOrs";
    //HashMap that stores all the markers and the respective carpark IDs
    private Map<String, String> mMarkerMap = new HashMap<>();
    //HashMap that stores all the distance from chosen location and the respective carpark ID
    private Map<String, Double> mDistanceMap = new HashMap<>();
    //Another HashMap that stores all the distance from chosen location and the respective carpark ID
    private Map<String, Double> mDistanceMapNew = new HashMap<>();
    //HashMap that stores sorted version of mDistanceMap
    private Map<String, Double> sortedDistanceMap = new HashMap<>();
    //HashMap that stores sorted version of mDistanceMapNew
    private Map<String, Double> sortedDistanceMapNew = new HashMap<>();
    //Variable that stores the current marker
    private Marker currentMarker;
    //Button for the bookmark option
    private ImageButton starBTN;
    //Variable that calls the carpark dao helper
    private CarparkDao carparkDaoHelper;
    //Variable that calls the user data dao helper
    private UserDataDao userDataDaoHelper;
    //Variable that calls the recycler view to display nearby carparks
    private RecyclerView recyclerView;
    //Recycler Adapter that help creates the recycler view
    private ListMapAdapter recyclerAdapter;
    //List reused from book marked carparks to store nearby carparks
    private List<BookmarkedCarpark> nearbyCarparks;
    //Buttons for settings icon and tutorial icon
    private ImageButton settingsButton_IMGBTN, tutorial_IMGBTN;


    /**
     * onCreate is used to initialise the activity.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        displayLocationSettingsRequest(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        carparkDaoHelper = new CarparkDaoImpl(this);
        userDataDaoHelper = new UserDataDaoFirebaseImpl();

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

        autocompleteSupportFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS));
        autocompleteSupportFragment.setCountry("SG");
        autocompleteSupportFragment.setOnPlaceSelectedListener(
                new PlaceSelectionListener() {
                    @Override
                    public void onPlaceSelected(Place place) {
                        LatLng latLng = place.getLatLng();
                        if (currentMarker != null) {
                            currentMarker.remove();
                        }
                        Location searchedLocation = new Location("");
                        searchedLocation.setLongitude(place.getLatLng().longitude);
                        searchedLocation.setLatitude(place.getLatLng().latitude);
                        MarkerOptions markerOption = new MarkerOptions();
                        markerOption.position(latLng);
                        markerOption.title(place.getName());
                        currentMarker = googleMap.addMarker(markerOption);
                        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

                        Toast.makeText(MapsActivity.this, "" + latLng.latitude, Toast.LENGTH_SHORT).show();

                        nearbyCarparks.clear();
                        nearbyCarparks = new ArrayList<>();

                        carparkDaoHelper.getAllCarparkEntireFullDetails(new NetworkCallEventListener() {
                            @RequiresApi(api = Build.VERSION_CODES.N)
                            @Override
                            public <T> void onComplete(T networkCallResult, Boolean isSuccessful, String errorMessage) {
                                if (isSuccessful) {
                                    carparks = (ArrayList<Carpark>) networkCallResult;
                                    double fullness;
                                    double distance;
                                    Location mark = new Location("");
                                    for (int counter = 0; counter < carparks.size(); counter++) {
                                        mark.setLatitude(carparks.get(counter).latitude);
                                        mark.setLongitude(carparks.get(counter).longitude);
                                        distance = searchedLocation.distanceTo(mark);
                                        mDistanceMapNew.put(carparks.get(counter).carparkNo, distance);
                                    }

                                } else {
                                    //deal with the error message, maybe toast or something
                                }
                                sortedDistanceMapNew = mDistanceMapNew.entrySet()
                                        .stream()
                                        .sorted(Map.Entry.comparingByValue())
                                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
                                int counter = 0;
                                nearbyCarparks = new ArrayList<>();
                                for (String name : sortedDistanceMapNew.keySet()) {
                                    if (counter <= 10) {
                                        nearbyCarparks.add(new BookmarkedCarpark(name));
                                        counter++;
                                    } else {
                                        break;
                                    }
                                }
                                recyclerAdapter.notifyDataSetChanged();
                                initRecyclerView();

                                for (BookmarkedCarpark bookmarkedCarpark : nearbyCarparks) {
                                    carparkDaoHelper.getCarparkDetailsByID(bookmarkedCarpark.getCarparkID(), new NetworkCallEventListener() {
                                        @Override
                                        public <T> void onComplete(T networkCallResult, Boolean isSuccessful, String errorMessage) {
                                            if (isSuccessful) {
                                                Carpark carpark = (Carpark) networkCallResult;
                                                bookmarkedCarpark.setCarparkName(carpark.carparkName);
                                                recyclerAdapter.notifyDataSetChanged();
                                            }

                                        }
                                    });

                                    carparkDaoHelper.getCarparkLiveAvailability(bookmarkedCarpark.getCarparkID(), new NetworkCallEventListener() {
                                        @Override
                                        public <T> void onComplete(T networkCallResult, Boolean isSuccessful, String errorMessage) {
                                            if (isSuccessful) {
                                                Integer availability = (Integer) networkCallResult;
                                                bookmarkedCarpark.setAvailability(availability.toString());
                                                recyclerAdapter.notifyDataSetChanged();
                                            }

                                        }
                                    });

                                }
                            }
                        });
                    }

                    @Override
                    public void onError(Status status) {
                        Toast.makeText(MapsActivity.this, "" + status.getStatusMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLocation();
        starBTN = findViewById(R.id.starButton);
        if (!userDataDaoHelper.isLoggedIn()) {
            starBTN.setVisibility(View.INVISIBLE);
        }
        starBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MapsActivity.this, UserBookmarksActivity.class);
                startActivity(intent);
            }
        });

        settingsButton_IMGBTN = findViewById(R.id.settings);
        settingsButton_IMGBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MapsActivity.this, AccountOptionsActivity.class));
            }
        });

        expandableView();

        tutorial_IMGBTN = findViewById(R.id.tutorialButton);
        tutorial_IMGBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openHelpDialog();
            }
        });
    }

    /**
     * Function to open up the help dialogue
     */

    public void openHelpDialog() {
        Dialog help = new Dialog(MapsActivity.this);
        help.requestWindowFeature(Window.FEATURE_NO_TITLE);
        help.setContentView(R.layout.maps_activity_tutorial);
        help.show();
    }

    /**
     * Function to ask user to turn on GPS
     * @param context to state the context the prompt need to be opened in
     */
    private void displayLocationSettingsRequest(Context context) {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override

            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(MapsActivity.this, 0x1);

                        } catch (IntentSender.SendIntentException e) {
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });
    }

    /**
     * Function that converts all the carpark icons into bitmap
     */
    private void makeBitmap() {
        BitmapDrawable g = (BitmapDrawable) getResources().getDrawable(R.drawable.green_car);
        BitmapDrawable y = (BitmapDrawable) getResources().getDrawable(R.drawable.yellow_car);
        BitmapDrawable r = (BitmapDrawable) getResources().getDrawable(R.drawable.red_car);
        BitmapDrawable b = (BitmapDrawable) getResources().getDrawable(R.drawable.black_car);
        BitmapDrawable gr = (BitmapDrawable) getResources().getDrawable(R.drawable.grey_car);
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

    /**
     * Function to fetch current user location
     */
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
                } else {
                    Location NTU = new Location("");
                    NTU.setLatitude(1.3483);
                    NTU.setLongitude(103.6831);
                    currentLocation = NTU;
                    Toast.makeText(getApplicationContext(), currentLocation.getLatitude() + "" + currentLocation.getLongitude(), Toast.LENGTH_SHORT).show();
                    SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    assert supportMapFragment != null;
                    supportMapFragment.getMapAsync(MapsActivity.this);
                }
            }
        });
    }

    /**
     * Function to start expandable view
     */
    public void expandableView() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); //this also can be done in XML
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        nearbyCarparks = new ArrayList<>();
        initRecyclerView();
    }

    /**
     * Function to initialize Google Map
     * @param googleMaps stores the instance of Google Map in this class
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onMapReady(GoogleMap googleMaps) {
        googleMap = googleMaps;
        makeBitmap();
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.style));


        carparkDaoHelper.getAllCarparkEntireFullDetails(new NetworkCallEventListener() {
            @Override
            public <T> void onComplete(T networkCallResult, Boolean isSuccessful, String errorMessage) {
                if (isSuccessful) {
                    carparks = (ArrayList<Carpark>) networkCallResult;
                    double fullness;
                    int availability = 100;
                    int capacity;
                    int total = 300;
                    double distance;
                    Location mark = new Location("");
                    for (int counter = 0; counter < carparks.size(); counter++) {
                        mark.setLatitude(carparks.get(counter).latitude);
                        mark.setLongitude(carparks.get(counter).longitude);
                        distance = currentLocation.distanceTo(mark);
                        if (carparks.get(counter).carDetails.liveAvailability != null && carparks.get(counter).carDetails.capacity != null) {
                            fullness = carparks.get(counter).carDetails.liveAvailability.doubleValue() / carparks.get(counter).carDetails.capacity.doubleValue();

                            if (fullness >= 0.7 && fullness <= 1) {
                                Marker marker = googleMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(mark.getLatitude(), mark.getLongitude()))
                                        .icon(BitmapDescriptorFactory.fromBitmap(greenParking)));
                                mMarkerMap.put(marker.getId(), carparks.get(counter).carparkNo);
                                mDistanceMap.put(carparks.get(counter).carparkNo, distance);

                            } else if (fullness >= 0.4 && fullness < 0.7) {
                                Marker marker = googleMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(mark.getLatitude(), mark.getLongitude()))
                                        .icon(BitmapDescriptorFactory.fromBitmap(yellowParking)));
                                mMarkerMap.put(marker.getId(), carparks.get(counter).carparkNo);
                                mDistanceMap.put(carparks.get(counter).carparkNo, distance);

                            } else if (fullness > 0 && fullness < 0.4) {
                                Marker marker = googleMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(mark.getLatitude(), mark.getLongitude()))
                                        .icon(BitmapDescriptorFactory.fromBitmap(redParking)));
                                mMarkerMap.put(marker.getId(), carparks.get(counter).carparkNo);
                                mDistanceMap.put(carparks.get(counter).carparkNo, distance);

                            } else {
                                Marker marker = googleMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(mark.getLatitude(), mark.getLongitude()))
                                        .icon(BitmapDescriptorFactory.fromBitmap(greyParking)));
                                mMarkerMap.put(marker.getId(), carparks.get(counter).carparkNo);
                                mDistanceMap.put(carparks.get(counter).carparkNo, distance);
                            }

                        } else {
                            Marker marker = googleMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(mark.getLatitude(), mark.getLongitude()))
                                    .icon(BitmapDescriptorFactory.fromBitmap(blackParking)));
                            mMarkerMap.put(marker.getId(), carparks.get(counter).carparkNo);
                            mDistanceMap.put(carparks.get(counter).carparkNo, distance);
                        }

                    }

                } else {
                    //deal with the error message, maybe toast or something
                }
                sortedDistanceMap = mDistanceMap.entrySet()
                        .stream()
                        .sorted(Map.Entry.comparingByValue())
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

                int counter = 0;
                nearbyCarparks = new ArrayList<>();
                for (String name : sortedDistanceMap.keySet()) {
                    if (counter <= 10) {
                        nearbyCarparks.add(new BookmarkedCarpark(name));
                        counter++;
                    } else {
                        break;
                    }
                }


                for (BookmarkedCarpark bookmarkedCarpark : nearbyCarparks) {
                    carparkDaoHelper.getCarparkDetailsByID(bookmarkedCarpark.getCarparkID(), new NetworkCallEventListener() {
                        @Override
                        public <T> void onComplete(T networkCallResult, Boolean isSuccessful, String errorMessage) {
                            if (isSuccessful) {
                                Carpark carpark = (Carpark) networkCallResult;
                                bookmarkedCarpark.setCarparkName(carpark.carparkName);
                                recyclerAdapter.notifyDataSetChanged();
                            }

                        }
                    });

                    carparkDaoHelper.getCarparkLiveAvailability(bookmarkedCarpark.getCarparkID(), new NetworkCallEventListener() {
                        @Override
                        public <T> void onComplete(T networkCallResult, Boolean isSuccessful, String errorMessage) {
                            if (isSuccessful) {
                                Integer availability = (Integer) networkCallResult;
                                bookmarkedCarpark.setAvailability(availability.toString());
                                recyclerAdapter.notifyDataSetChanged();
                            }

                        }
                    });

                }

                initRecyclerView();
            }
        });
        int counter = 0;

        sortedDistanceMap = mDistanceMap.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("Current Location");

        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                String carparkID = mMarkerMap.get(marker.getId());
                LatLng latLng = marker.getPosition();
                double longnitude = latLng.longitude;
                double latitude = latLng.latitude;
                Intent intent = new Intent(MapsActivity.this, DetailCarparkActivity.class);
                intent.putExtra("CARPARK_ID", carparkID);
                intent.putExtra("Lat", latitude);
                intent.putExtra("Lng", longnitude);
                startActivity(intent);
                return false;
            }
        });
        googleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                if (currentMarker != null) {
                    currentMarker.remove();
                }
                fetchLocation();
                nearbyCarparks.clear();
                googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

                nearbyCarparks.clear();
                nearbyCarparks = new ArrayList<>();

                System.out.println(nearbyCarparks);
                int counter = 0;
                for (String name : sortedDistanceMap.keySet()) {
                    if (counter <= 10) {
                        nearbyCarparks.add(new BookmarkedCarpark(name));
                        counter++;
                    } else
                        break;
                }


                for (BookmarkedCarpark bookmarkedCarpark : nearbyCarparks) {
                    carparkDaoHelper.getCarparkDetailsByID(bookmarkedCarpark.getCarparkID(), new NetworkCallEventListener() {
                        @Override
                        public <T> void onComplete(T networkCallResult, Boolean isSuccessful, String errorMessage) {
                            if (isSuccessful) {
                                Carpark carpark = (Carpark) networkCallResult;
                                bookmarkedCarpark.setCarparkName(carpark.carparkName);
                                recyclerAdapter.notifyDataSetChanged();
                            }

                        }
                    });

                    carparkDaoHelper.getCarparkLiveAvailability(bookmarkedCarpark.getCarparkID(), new NetworkCallEventListener() {
                        @Override
                        public <T> void onComplete(T networkCallResult, Boolean isSuccessful, String errorMessage) {
                            if (isSuccessful) {
                                Integer availability = (Integer) networkCallResult;
                                bookmarkedCarpark.setAvailability(availability.toString());
                                recyclerAdapter.notifyDataSetChanged();
                            }

                        }
                    });

                }
                recyclerAdapter.notifyDataSetChanged();
                return true;
            }
        });
    }

    
    /**
     * Function to initialize expandable view
     */
    private void initRecyclerView() {
        recyclerAdapter = new ListMapAdapter(nearbyCarparks, this, this);
        recyclerView.setAdapter(recyclerAdapter);
    }

    /**
     * Function that that zooms to the specific carpark when pressed on the expandable view
     * @param position states which specific list item the user pressed
     */
    @Override
    public void onUserClicked(int position) {
        String name = nearbyCarparks.get(position).getCarparkID();
        carparkDaoHelper.getCarparkDetailsByID(name, new NetworkCallEventListener() {
            @Override
            public <T> void onComplete(T networkCallResult, Boolean isSuccessful, String errorMessage) {
                Carpark carpark = (Carpark) networkCallResult;
                double latitude = carpark.latitude;
                double longitude = carpark.longitude;
                LatLng latLng = new LatLng(latitude, longitude);
                if (currentMarker != null) {
                    currentMarker.remove();
                }
                MarkerOptions markerOptions2 = new MarkerOptions().position(latLng);
                currentMarker = googleMap.addMarker(markerOptions2);
                googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
            }
        });
    }


}