package com.example.somiti;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class Somiti extends Application {


    @Override
    public void onCreate() {
        super.onCreate();


        FirebaseDatabase.getInstance().setPersistenceEnabled(true);



    }
}
