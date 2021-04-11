package com.example.myapplication.interfaces;

import com.example.myapplication.model.heroModel;

import java.util.ArrayList;

public interface VolleyCallback{
    void onSuccess(ArrayList<heroModel> heros);
    void onError();
}
