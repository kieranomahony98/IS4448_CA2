package com.example.myapplication.userInterations.covid;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.api.CovidApiHandler;
import com.example.myapplication.data.Covid19DataHolder;
import com.example.myapplication.interfaces.CovidCallback;
import com.example.myapplication.model.Covid19Data;
import com.google.gson.Gson;

import java.util.ArrayList;


public class view_all_covid extends Fragment {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    CovidRecylerAdapter rAdapter;
    ArrayList<Covid19Data> covid19Data;
    ProgressBar pbLoading;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_view_all_covid, container, false);
        recyclerView = v.findViewById(R.id.rvCovid);
        pbLoading = v.findViewById(R.id.pbLoadingCovid);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        CovidApiHandler covidApiHandler = CovidApiHandler.getInstance();
        final Covid19DataHolder covid19DataHolder = Covid19DataHolder.getInstance();
        covid19Data = covid19DataHolder.getList();

        if(covid19Data != null){
            pbLoading.setVisibility(View.INVISIBLE);
            createRecyclerView();
            return;
        }

        covidApiHandler.getLiveData(getContext(), new CovidCallback() {
            @Override
            public void onSuccess(ArrayList<Covid19Data> covidData) {
                covid19DataHolder.setList(covid19Data);
                covid19Data = covidData;
                pbLoading.setVisibility(View.INVISIBLE);
                createRecyclerView();
            }

            @Override
            public void onError(String message) {
                Log.e("Covid Error", "failed to get data: " + message);
                Toast.makeText(getContext(), "Failed to get Covid Data, please try again later", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createRecyclerView() {
        try {
            try {
                recyclerView.setHasFixedSize(true);
                layoutManager = new LinearLayoutManager(this.getContext());
                recyclerView.setLayoutManager(layoutManager);
                rAdapter = new CovidRecylerAdapter(covid19Data, this.getContext());
                rAdapter.setOnItemClickListener(onItemClickListener);

                recyclerView.setAdapter(rAdapter);

            } catch (Exception e) {
                Log.e("Recycler View Fragment", "Error creating the recycler view" + e.getMessage());
                throw e;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadFragment(Covid19Data covid19Data) {
        Gson gson = new Gson();
        Fragment fragment = new bar_chart();
        Bundle bundle = new Bundle();
        bundle.putString("covid19Obj", gson.toJson(covid19Data));
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fgMain, fragment);
        fragmentTransaction.commit();
    }

    private View.OnClickListener onItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
            loadFragment(covid19Data.get(viewHolder.getAdapterPosition()));
        }
    };
}