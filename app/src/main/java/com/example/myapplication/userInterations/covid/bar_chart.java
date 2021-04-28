package com.example.myapplication.userInterations.covid;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.example.myapplication.model.Covid19Data;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class bar_chart extends Fragment {
    BarChart covidLineChart;
//    ArrayList<Covid19Data> covid19Data;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_bar_chart, container, false);
        if(getArguments() == null){
            return v;
        }

        covidLineChart = v.findViewById(R.id.covidBarChart);
        Gson gson  = new Gson();
        createBarChart(gson.fromJson(getArguments().getString("covid19Obj"), Covid19Data.class));
        return v;
    }
    private void createBarChart(Covid19Data covid19Data){
        List<BarEntry> barEntries= new ArrayList<>();
        barEntries.add(new BarEntry(0f, covid19Data.getConfirmed()));
        barEntries.add(new BarEntry(1f, covid19Data.getRecovered()));
        barEntries.add(new BarEntry(2f, covid19Data.getDeaths()));
        String[] labels = {"Confirmed", "Recovered", "Deaths"};
        BarDataSet set = new BarDataSet(barEntries, "");
        set.setColors(new int[] {Color.RED, Color.GREEN, Color.BLUE});

        XAxis xAxis =  covidLineChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        BarData barData = new BarData(set);
        covidLineChart.setData(barData);
        covidLineChart.invalidate();

    }


}
