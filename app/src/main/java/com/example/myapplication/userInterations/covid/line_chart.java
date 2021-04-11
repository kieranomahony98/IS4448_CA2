package com.example.myapplication.userInterations.covid;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.api.CovidApiHandler;
import com.example.myapplication.interfaces.CovidCallback;
import com.example.myapplication.model.Covid19Data;
import com.example.myapplication.model.heroModel;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class line_chart extends Fragment {
    LineChart covidLineChart;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_line_chart, container, false);
        covidLineChart = v.findViewById(R.id.lcCovid);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        getChartData();
    }

    private void getChartData(){
        CovidApiHandler covidApiHandler = new CovidApiHandler();
        covidApiHandler.getLiveData(getContext(), new CovidCallback() {
            @Override
            public void onSuccess(ArrayList<Covid19Data> covid19Data) {
                int length = covid19Data.size() -1;
                if(length > 7){
                    covid19Data = (ArrayList<Covid19Data>) covid19Data.subList(length -7, length);
                }
                createChart(covid19Data);
            }

            @Override
            public void onError(String message) {

            }
        });
    }
    private void createChart(ArrayList<Covid19Data> covid19Data){
        List<Entry> positiveFigures = new ArrayList<>();
        List<Entry> recoveredFigures = new ArrayList<>();
        List<Entry> deathFigures = new ArrayList<>();
        List<String> xAxisValues = new ArrayList<>();
        for(int i =0; i < covid19Data.size(); i++){
            positiveFigures.add(new Entry(i, covid19Data.get(i).getConfirmed()));
            recoveredFigures.add(new Entry( i, covid19Data.get(i).getRecovered()));
            deathFigures.add(new Entry( i, covid19Data.get(i).getDeaths()));
            xAxisValues.add(covid19Data.get(i).getDate().split("T")[0]);
        }

        XAxis xAxis = covidLineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xAxisValues));
        LineDataSet positiveDataSet = new LineDataSet(positiveFigures, "Positive");
        LineDataSet deathDataSet = new LineDataSet(deathFigures, "Deaths");
        LineDataSet recoveredDataSet = new LineDataSet(recoveredFigures, "Recovered");

        positiveDataSet.setColor(R.color.colorAccent);

        List<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(positiveDataSet);
        dataSets.add(recoveredDataSet);
        dataSets.add(deathDataSet);

        LineData data = new LineData(dataSets);
        covidLineChart.setData(data);
        covidLineChart.invalidate();
    }
}