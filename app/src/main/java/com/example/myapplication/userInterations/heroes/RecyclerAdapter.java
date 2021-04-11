package com.example.myapplication.userInterations.heroes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapplication.R;
import com.example.myapplication.model.heroModel;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {
    Context context;
    List<heroModel> heroes;
    private View.OnClickListener ocListener;


    public RecyclerAdapter(List<heroModel> heroes, Context context) {
        this.heroes = heroes;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_hero_single_line, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tvName.setText(heroes.get(position).getName());
        holder.tvRealName.setText(heroes.get(position).getRealName());
        holder.tvRating.setText(String.valueOf(heroes.get(position).getRating()));
        holder.tvAffiliation.setText(heroes.get(position).getTeamAffiliation());
        String imagePath = heroes.get(position).getTeamAffiliation();

        switch (imagePath) {
            case "Avengers":
                imagePath = "https://seeklogo.com/images/T/the-avengers-logo-0BF397F78A-seeklogo.com.png";
                break;
            case "X-Men":
                imagePath = "https://seeklogo.com/images/X/x-men-logo-F16A0D1D4D-seeklogo.com.png";
                break;
            case "Justice League":
                imagePath = "https://i.pinimg.com/600x315/d2/32/98/d23298e8be6b9f7aa533e283228c4c2b.jpg";
                break;
            case "Fantastic Four":
                imagePath = "https://img.icons8.com/metro/452/fantastic-four.png";
                break;
            default:
                imagePath = "https://lh3.googleusercontent.com/proxy/I2lQkCQE8OJS_LTb6mF5HMeoBN-jjv7-XyMW2HgTLkBNuA3Al6seu0Jb-FGEZjhnJV_4yNE9EUPbmAbijPwHmgHUCHXEjPM";
                break;
        }
        Glide.with(context).load(imagePath).apply(new RequestOptions().override(100, 100)).into(holder.ivLogo);
    }


    @Override
    public int getItemCount() {
        return heroes.size();
    }

    public void filterList(ArrayList<heroModel> heroModels){
        heroes = heroModels;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvRealName, tvRating, tvAffiliation;
        ImageView ivLogo;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvRealName = itemView.findViewById(R.id.tvRealName);
            tvRating = itemView.findViewById(R.id.tvRating);
            tvAffiliation = itemView.findViewById(R.id.tvAffiliation);
            ivLogo = itemView.findViewById(R.id.ivLogo);
            itemView.setTag(this);
        }
    }
}
