package com.example.myapplication.api;

import android.content.Context;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.interfaces.CreateHero;
import com.example.myapplication.interfaces.DeleteHero;
import com.example.myapplication.interfaces.VolleyCallback;
import com.example.myapplication.model.heroModel;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HeroApiHandler {
    private static HeroApiHandler heroApiHandler = null;
    public final String url = "https://gleeson.io/IS4447/HeroAPI/v1/Api.php?apicall=";
    Gson gson = new Gson();

    public static HeroApiHandler getInstance(){
        if(heroApiHandler == null){
            heroApiHandler = new HeroApiHandler();
        }
        return heroApiHandler;
    }

    public void getHeros(final VolleyCallback volleyCallback, Context context) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        Log.d("in here", "am i working");

        requestQueue.add(
                new JsonObjectRequest(Request.Method.GET, url + "getheroes", null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            ArrayList<heroModel> heroList = new ArrayList<heroModel>();

                            JSONArray jsonArray = response.getJSONArray("heroes");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                heroList.add(gson.fromJson(String.valueOf(jsonArray.getJSONObject(i)), heroModel.class));
                            }
                            volleyCallback.onSuccess(heroList);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("HERO API", "onErrorResponse: " + error.getMessage());
                        volleyCallback.onError();
                    }
                }));

    }

    public void createHero(final CreateHero createHero, final heroModel h, Context context) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        requestQueue.add(new StringRequest(Request.Method.POST, url + "createhero", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("test", response);
                try {
                    JSONObject res = new JSONObject(response);
                    if (res.getBoolean("error") != false) {
                        createHero.onError(res.getString("message"));
                        return;
                    }
                    if (res.getJSONArray("heroes").get(0) == null) {
                        createHero.onError("Unknown Error: Hero could not be added");
                        return;
                    }
                    createHero.onSuccess(gson.fromJson(String.valueOf(res.getJSONArray("heroes").get(0)), heroModel.class));
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("res", error.getMessage());

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", h.getName());
                params.put("realname", h.getRealName());
                params.put("rating", String.valueOf(h.getRating()));
                params.put("teamaffiliation", String.valueOf(h.getTeamAffiliation()));
                return params;
            }
        });
    }

    public void deleteHero(int id, Context context, final DeleteHero deleteHeroCallback) {

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        requestQueue.add(new StringRequest(Request.Method.DELETE, url + "deletehero&id=" + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    if (res.getBoolean("error") != false) {
                        deleteHeroCallback.onError(res.getString("message"));
                        return;
                    }
                    deleteHeroCallback.onSuccess();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                deleteHeroCallback.onError(error.getMessage());
            }
        }));

    }

    public void updateHero(final CreateHero createHeroCallback, Context context, final heroModel h) {
        Log.d("", "updateHero: "+ h.getId());
        Log.d("", "updateHero: "+ h.getName());
        Log.d("", "updateHero: "+ h.getRealName());
        Log.d("", "updateHero: "+ h.getRating());

        final Gson gson = new Gson();
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(new StringRequest(Request.Method.POST, url + "updatehero&id=" + h.getId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    if(res.getBoolean("error") != false){
                        createHeroCallback.onError(res.getString("message"));
                    }

                    if(res.getJSONArray("heroes").get(0) != null){
                        createHeroCallback.onSuccess(gson.fromJson(String.valueOf(res.getJSONArray("heroes").get(0)), heroModel.class));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id",String.valueOf(h.getId()));
                params.put("name", h.getName());
                params.put("realname", h.getRealName());
                params.put("rating", String.valueOf(h.getRating()));
                params.put("teamaffiliation", String.valueOf(h.getTeamAffiliation()));
                return params;
            }
        });
    }
}


