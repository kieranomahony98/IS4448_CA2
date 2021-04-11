package com.example.myapplication.interfaces;

import com.example.myapplication.model.Covid19Data;

import java.util.ArrayList;

public interface CovidCallback {
    public void onSuccess(ArrayList<Covid19Data> covid19Data);
    public void onError(String message);


}
