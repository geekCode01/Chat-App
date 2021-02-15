package com.example.chatapp;

import android.bluetooth.BluetoothClass;
import android.os.Build;

import com.google.firebase.database.DatabaseReference;

import java.io.LineNumberReader;

public class ErrorClass {
    ErrorClass(){}

    private String BRAND="unknown";
    private String M="unknown";
    private String P="unknown";
    private String D="unknown";
    private String DEVICE="unknown";

    void sendError(DatabaseReference myErrorRef,String LineNumber,String errorMsg,String funcName)
    {
        BRAND= Build.BRAND;
        M=Build.MODEL;
        P=Build.PRODUCT;
        D=Build.DEVICE;

        DEVICE="M: " + M + " P: " + P + " D: " + D;
        String key=myErrorRef.child(funcName).child(BRAND).child(DEVICE).push().getKey();
        assert key != null;
        myErrorRef.child(funcName).child(BRAND).child(DEVICE).child(key).child("msg").setValue(errorMsg);
        myErrorRef.child(funcName).child(BRAND).child(DEVICE).child(key).child("line").setValue(LineNumber);
    }

}


