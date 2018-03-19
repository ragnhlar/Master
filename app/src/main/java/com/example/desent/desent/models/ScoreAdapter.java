package com.example.desent.desent.models;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.desent.desent.R;

import java.util.List;

/**
 * Created by ragnhildlarsen on 18.03.2018.
 */

public class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.ScoreViewHolder> {

    //this context will be used to inflate the layout
    private Context context;
    //storing all the friends in a list
    private List<Score> scoreList;

    public ScoreAdapter(Context context, List<Score> scoreList) {
        this.context = context;
        this.scoreList = scoreList;
    }

    @Override
    public ScoreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_leaderboard, null);
        return new ScoreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ScoreViewHolder holder, int position) {
        Score score = scoreList.get(position);
        System.out.println("Position onbindviewholder: " + position);
        if (position == 0){
            holder.tvNameFirstPlace.setText(score.getName());
            holder.tvAvgCfFirstPlace.setText(String.valueOf(score.getAvg_cf()));
            holder.imgFirstPlace.setImageDrawable(context.getResources().getDrawable(score.getImage()));
        }
        if (position == 1){
            holder.tvNameSecondPlace.setText(score.getName());
            holder.tvAvgCfSecondPlace.setText(String.valueOf(score.getAvg_cf()));
            holder.imgSecondPlace.setImageDrawable(context.getResources().getDrawable(score.getImage()));
        }
        if (position == 2){
            holder.tvNameThirdPlace.setText(score.getName());
            holder.tvAvgCfThirdPlace.setText(String.valueOf(score.getAvg_cf()));
            holder.imgThirdPlace.setImageDrawable(context.getResources().getDrawable(score.getImage()));
        }
        //binding the data with the viewholder views
        holder.tvPlaceOnLeaderboard.setText(position + 1 + ".");
        holder.tvName.setText(score.getName());
        holder.tvNumCoins.setText(String.valueOf(score.getNum_coins()));
        Resources resources = context.getResources();
        holder.tvAvgCf.setText(String.valueOf(score.getAvg_cf()) + " " + resources.getString(R.string.carbon_footprint_unit));

        holder.image.setImageDrawable(context.getResources().getDrawable(score.getImage()));

    }

    @Override
    public int getItemCount() {
        return scoreList.size();
    }

    public class ScoreViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvNumCoins, tvAvgCf;
        ImageView image;
        TextView tvPlaceOnLeaderboard, tvNameFirstPlace, tvAvgCfFirstPlace,
                tvNameSecondPlace, tvAvgCfSecondPlace, tvNameThirdPlace, tvAvgCfThirdPlace;
        ImageView imgFirstPlace, imgSecondPlace, imgThirdPlace;

        FrameLayout frameLayout;

        public ScoreViewHolder(View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.textViewName);
            tvNumCoins = itemView.findViewById(R.id.textViewNumCoins);
            tvAvgCf = itemView.findViewById(R.id.textViewAvgCf);
            image = itemView.findViewById(R.id.imageView);
            tvPlaceOnLeaderboard = itemView.findViewById(R.id.tvPlaceOnLeaderboard);
            tvNameFirstPlace = itemView.findViewById(R.id.tvNameFirstPlace);
            tvAvgCfFirstPlace = itemView.findViewById(R.id.tvAvgCfFirstPlace);
            tvNameSecondPlace = itemView.findViewById(R.id.tvNameSecondPlace);
            tvAvgCfSecondPlace = itemView.findViewById(R.id.tvAvgCfSecondPlace);
            tvNameThirdPlace = itemView.findViewById(R.id.tvNameThirdPlace);
            tvAvgCfThirdPlace = itemView.findViewById(R.id.tvAvgCfThirdPlace);
            imgFirstPlace = itemView.findViewById(R.id.imgFirstPlace);
            imgSecondPlace = itemView.findViewById(R.id.imgSecondPlace);
            imgThirdPlace = itemView.findViewById(R.id.imgThirdPlace);
            frameLayout = itemView.findViewById(R.id.frameLayoutPodium);
        }
    }
}
