package com.example.myapplication.model;

import com.android.volley.Response;

import org.json.JSONObject;

public class heroModel implements Response.Listener<JSONObject> {
    private int id;
    private String name;
    private String realname;
    private int rating;
    private String teamaffiliation;

    public heroModel(int id, String name, String realName, int rating, String teamAffiliation){
        this.id = id;
        this.name = name;
        this.realname = realName;
        this.rating = rating;
        this.teamaffiliation = teamAffiliation;
    }

    public String getTeamAffiliation() {
        return teamaffiliation;
    }

    public void setTeamAffiliation(String teamAffiliation) {
        this.teamaffiliation = teamAffiliation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRealName() {
        return realname;
    }

    public void setRealName(String realName) {
        this.realname = realName;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    @Override
    public void onResponse(JSONObject response) {

    }
}
