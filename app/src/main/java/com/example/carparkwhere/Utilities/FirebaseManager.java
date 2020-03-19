package com.example.carparkwhere.Utilities;

import android.app.Activity;
import android.content.Context;


import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;


public class FirebaseManager {

    private static FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();

    public enum CollectionsName
    {
        USERS("users"),
        FAVOURITE_CARPARKS("favouriteCarparks"),
        CARPARKREVIEWS("carparkReviews");

        private String collectionName;

        CollectionsName(String collectionName) {
            this.collectionName = collectionName;
        }

        public String getString() {
            return this.collectionName;
        }
    }



    public static FirebaseUser getCurrentUser(){
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        return currentUser;
    }


    public static void createNewUser(Context context, final String email, String password, final String displayName, OnCompleteListener<AuthResult> handler){
        mAuth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener((Activity) context, handler)
        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

                getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //in future need to check whether this user is email verified before logging in
                        Map<String,Object> user = new HashMap<>();
                        user.put("displayName",displayName);
                        user.put("email",email);
                        FirebaseManager.insertToFirestore(CollectionsName.USERS.getString(), getCurrentUser().getUid(), user, new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                            }
                        });
                    }
                });
            }
        });
    }

    public static void signInWithEmail(Context context, String email, String password, OnCompleteListener<AuthResult> handler){
        mAuth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener((Activity) context,handler);
    }



    public static void insertToFirestore(String collectionName, String documentName, Object data, OnCompleteListener<Void> handler){
        db.collection(collectionName).document(documentName)
                .set(data)
                .addOnCompleteListener(handler);
    }

    public static void updateFieldFirestore(String collectionName, String documentName, Map<String,Object> data){
        db.collection(collectionName).document(documentName).set(data,SetOptions.merge());
    }

    public static void updateFieldFirestoreArray(String collectionName, String documentName, Map<String,Object> data){
        db.collection(collectionName).document(documentName).update(data);
    }



    public static void retrieveFromFirestore(String collectionName, String documentName, OnCompleteListener<DocumentSnapshot> handler) {
        DocumentReference docRef = db.collection(collectionName).document(documentName);
        docRef.get().addOnCompleteListener(handler);
    }

}
