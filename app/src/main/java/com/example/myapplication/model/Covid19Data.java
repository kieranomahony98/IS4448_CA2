package com.example.myapplication.model;

public class Covid19Data {
    private int Deaths;
    private int Recovered;
    private int Confirmed;
    private String Date;
    public Covid19Data(int deaths, int recovered, int confirmed, String date){
        this.setDeaths(deaths);
        this.setRecovered(recovered);
        this.setConfirmed(confirmed);
        this.setDate(date);
    }


    public int getDeaths() {
        return Deaths;
    }

    public void setDeaths(int deaths) {
        this.Deaths = deaths;
    }

    public int getRecovered() {
        return Recovered;
    }

    public void setRecovered(int recovered) {
        this.Recovered = recovered;
    }

    public int getConfirmed() {
        return Confirmed;
    }

    public void setConfirmed(int confirmed) {
        this.Confirmed = confirmed;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        this.Date = date;
    }
}
