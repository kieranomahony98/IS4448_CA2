package com.example.myapplication.api;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.interfaces.CovidCallback;
import com.example.myapplication.model.Covid19Data;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CovidApiHandler {
    final String url = "https://api.covid19api.com/live/country/ireland";
    private static CovidApiHandler covidApiHandler;

    public static CovidApiHandler getInstance(){
        if(covidApiHandler == null){
            covidApiHandler = new CovidApiHandler();
        }
        return covidApiHandler;
    }

    public void getLiveData(Context context, final CovidCallback covidCallback) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(
                new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        ArrayList<Covid19Data> covid19Data = new ArrayList<>();
                        Gson gson = new Gson();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                covid19Data.add(gson.fromJson(String.valueOf(response.get(i)), Covid19Data.class));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        covidCallback.onSuccess(covid19Data);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        covidCallback.onError(error.getMessage());
                    }
                }));
    }
}
