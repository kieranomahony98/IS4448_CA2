package com.example.myapplication.data;

import android.content.Context;

import com.example.myapplication.api.CovidApiHandler;
import com.example.myapplication.interfaces.CovidCallback;
import com.example.myapplication.model.Covid19Data;

import java.util.ArrayList;

import static java.security.AccessController.getContext;

public class Covid19DataHolder {

    private static Covid19DataHolder covid19DataHolderInstance;
    private ArrayList<Covid19Data> covid19Data = null;
    Context context;
    public static Covid19DataHolder getInstance(){
        if(covid19DataHolderInstance == null){
            covid19DataHolderInstance = new Covid19DataHolder();
        }
        return  covid19DataHolderInstance;
    }

    private Covid19DataHolder(){
        covid19Data = new ArrayList<Covid19Data>();
    }
    public ArrayList<Covid19Data> getList(){
        return this.covid19Data;
    }

    public void setList(ArrayList<Covid19Data> covid19Data){
        this.covid19Data = covid19Data;
    }
}
