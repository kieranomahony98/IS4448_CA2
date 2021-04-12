package com.example.myapplication.userInterations.covid;

import android.graphics.Color;
import android.graphics.ColorSpace;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
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
import java.util.stream.IntStream;

public class line_chart extends Fragment implements AdapterView.OnItemSelectedListener {
    LineChart covidLineChart;
    ProgressBar pbLineLoading;
    Spinner spDates;
    ArrayList<Covid19Data> covid19DataList;

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
        pbLineLoading = v.findViewById(R.id.pbLineLoading);
        spDates = v.findViewById(R.id.spDates);
        spDates.setOnItemSelectedListener(this);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        getChartData();

    }

    private void getChartData() {
        CovidApiHandler covidApiHandler = new CovidApiHandler();
        covidApiHandler.getLiveData(getContext(), new CovidCallback() {
            @Override
            public void onSuccess(ArrayList<Covid19Data> covid19Data) {
                covid19DataList = covid19Data;
                setDates();
                formatChartData();
            }

            @Override
            public void onError(String message) {
                Log.e("Error", "Failed to get covid data " + message);
                Toast.makeText(getContext(), "Failed to get latest Covid data, Please try again later", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int getStartIndex() {
        String[] dates = spDates.getSelectedItem().toString().split("-");
        String date = dates[0] + "-" + dates[1] + "-" + dates[2];

        int i = -1;
        for (Covid19Data c : covid19DataList) {
            if (c.getDate().split("T")[0].trim().equals(date.trim())) {
                i = covid19DataList.indexOf(c);
                break;
            }
        }
        return i;
    }

    private void formatChartData() {
        List<Entry> positiveFigures = new ArrayList<>();
        List<Entry> recoveredFigures = new ArrayList<>();
        List<Entry> deathFigures = new ArrayList<>();
        List<String> xAxisValues = new ArrayList<>();

        int start = getStartIndex();
        if (start == -1) {
            Toast.makeText(getContext(), "Failed to get covid data, please try again later", Toast.LENGTH_SHORT).show();
            return;
        }

        for (int i = start; i < start + 7; i++) {
            positiveFigures.add(new Entry(i, covid19DataList.get(i + 1).getConfirmed() - covid19DataList.get(i).getConfirmed()));
            recoveredFigures.add(new Entry(i, covid19DataList.get(i + 1).getRecovered() - covid19DataList.get(i).getRecovered()));
            deathFigures.add(new Entry(i, covid19DataList.get(i + 1).getDeaths() - covid19DataList.get(i).getDeaths()));
            xAxisValues.add(covid19DataList.get(i).getDate().split("T")[0]);
        }

        setChartData(positiveFigures, recoveredFigures, deathFigures, xAxisValues);
    }


    private void setChartData(List<Entry> positiveFigures, List<Entry> recoveredFigures, List<Entry> deathFigures, List<String> xAxisValues) {
        XAxis xAxis = covidLineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xAxisValues));

        LineDataSet positiveDataSet = new LineDataSet(positiveFigures, "Positive");
        positiveDataSet.setColor(Color.parseColor("#008000"));
        LineDataSet deathDataSet = new LineDataSet(deathFigures, "Deaths");
        deathDataSet.setColor(Color.parseColor("#696969"));
        LineDataSet recoveredDataSet = new LineDataSet(recoveredFigures, "Recovered");
        deathDataSet.setColor(Color.parseColor("#0000FF"));

        List<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(positiveDataSet);
        dataSets.add(recoveredDataSet);
        dataSets.add(deathDataSet);

        LineData data = new LineData(dataSets);
        covidLineChart.setData(data);
        covidLineChart.invalidate();

        pbLineLoading.setVisibility(View.INVISIBLE);
        covidLineChart.setVisibility(View.VISIBLE);
        spDates.setVisibility(View.VISIBLE);
    }

    public void setDates() {
        ArrayList<String> spDatesValuesList = new ArrayList<>();
        int i = 0;
        int length = covid19DataList.size() - 1;
        boolean isCompleted = false;

        do {
            if(i == length) break;

            if (i + 6 > length) {
                spDatesValuesList.add(covid19DataList.get(i).getDate().split("T")[0] + " - " + covid19DataList.get(length).getDate().split("T")[0]);
                isCompleted = true;
            } else {
                spDatesValuesList.add(covid19DataList.get(i).getDate().split("T")[0] + " - " + covid19DataList.get(i + 7).getDate().split("T")[0]);
                i += 8;
            }
        } while (!isCompleted);

        ArrayAdapter<String> spDatesValues = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, spDatesValuesList);
        spDatesValues.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDates.setAdapter(spDatesValues);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        covidLineChart.setVisibility(View.INVISIBLE);
        pbLineLoading.setVisibility(View.VISIBLE);
        formatChartData();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}