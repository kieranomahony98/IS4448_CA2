package com.example.myapplication.userInterations.covid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.model.Covid19Data;

import java.util.List;

public class CovidRecylerAdapter extends RecyclerView.Adapter<CovidRecylerAdapter.MyViewHolder> {
    Context context;
    List<Covid19Data> covidDataList;
    private View.OnClickListener ocListener;


    public CovidRecylerAdapter(List<Covid19Data> covid19Data, Context context) {
        this.covidDataList = covid19Data;
        this.context = context;
    }

    public void setOnItemClickListener(View.OnClickListener itemClickListener) {
        ocListener = itemClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_single_line_covid, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tvDate.setText(covidDataList.get(position).getDate().split("T")[0]);
        holder.tvRecovered.setText(String.valueOf(covidDataList.get(position).getRecovered()));
        holder.tvDeaths.setText(String.valueOf(covidDataList.get(position).getDeaths()));
        holder.tvConfirmed.setText(String.valueOf(covidDataList.get(position).getConfirmed()));

    }


    @Override
    public int getItemCount() {
        return covidDataList.size();
    }

//    public void filterList(ArrayList<heroModel> heroModels) {
//        heroes = heroModels;
//        notifyDataSetChanged();
//    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvDeaths, tvRecovered, tvConfirmed;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvConfirmed = itemView.findViewById(R.id.tvConfirmed);
            tvDeaths = itemView.findViewById(R.id.tvDeath);
            tvRecovered = itemView.findViewById(R.id.tvRecovered);
            itemView.setTag(this);
            itemView.setOnClickListener(ocListener);

        }
    }
}
