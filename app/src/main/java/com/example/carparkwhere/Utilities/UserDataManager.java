package com.example.carparkwhere.Utilities;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentSnapshot;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserDataManager {

    public static String getUserEmail(){
        return FirebaseManager.getCurrentUser().getEmail();
    }

    public static String getDisplayName(){
        return FirebaseManager.getCurrentUser().getDisplayName();
    }

    public static boolean isLoggedIn(){
        return (FirebaseManager.getCurrentUser() != null);
    }

    public static void updateDisplayName(String displayName){
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(displayName).build();
        FirebaseManager.getCurrentUser().updateProfile(profileUpdates);
        Map<String,Object> displayNameMap = new HashMap<>();
        displayNameMap.put("displayName",displayName);
        FirebaseManager.updateFieldFirestore("users",FirebaseManager.getCurrentUser().getUid(),displayNameMap);
    }

//    Map<String,Object> carparkMap = new HashMap<>();
//    ArrayList<String> carparks = new ArrayList<>();
//    carparks = (ArrayList<String>) task.getResult().get("carparks");

    public static void getFavouriteCarparksId(OnCompleteListener onCompleteListener){
        if (isLoggedIn()){
            FirebaseManager.retrieveFromFirestore(FirebaseManager.CollectionsName.FAVOURITE_CARPARKS.getString(), FirebaseManager.getCurrentUser().getUid(),onCompleteListener);
        }
    }

    public static void addNewFavouriteCarpark(final String carparkId){
        if (isLoggedIn()){
            FirebaseManager.retrieveFromFirestore(FirebaseManager.CollectionsName.FAVOURITE_CARPARKS.getString(), FirebaseManager.getCurrentUser().getUid(), new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    Map<String,Object> carparkMap = new HashMap<>();
                    ArrayList<String> carparks = new ArrayList<>();
                    carparks = (ArrayList<String>) task.getResult().get("carparks");
                    if (carparks == null){
                        carparks = new ArrayList<>();
                        carparks.add(carparkId);
                    }else{
                        if (!carparks.contains(carparkId)){carparks.add(carparkId);}
                    }
                    carparkMap.put("carparks",carparks);
                    FirebaseManager.updateFieldFirestore(FirebaseManager.CollectionsName.FAVOURITE_CARPARKS.getString(),FirebaseManager.getCurrentUser().getUid(),carparkMap);
                }
            });
        }
    }



}
