package com.example.desent.desent.models;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.desent.desent.R;

import java.util.List;

/**
 * Created by ragnhildlarsen on 17.03.2018.
 */

public class ChallengeAdapter extends RecyclerView.Adapter<ChallengeAdapter.ChallengeViewHolder>{

    //this context will be used to inflate the layout
    private Context context;
    //storing all the friends in a list
    private List<Challenge> challengeList;

    //getting the context and friend list with constructor
    public ChallengeAdapter(Context context, List<Challenge> challengeList){
        this.context = context;
        this.challengeList = challengeList;
    }

    @Override
    public ChallengeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_call_to_arms, null);
        return new ChallengeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ChallengeViewHolder holder, int position) {
        //getting the challenge of the specified position
        Challenge challenge = challengeList.get(position);
        //Challenge challenge = challengeList.get(position);

        //binding the data with the viewholder views
        holder.tvTitle.setText(challenge.getTitle());
        holder.tvDescription.setText(String.valueOf(challenge.getDescription()));
        holder.tvDuration.setText(String.valueOf(challenge.getDuration() + " days"));
        holder.tvPrize.setText("Earn " + challenge.getPrize());

        holder.btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Challenge is started. Good luck!", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return challengeList.size();
    }

    public class ChallengeViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle, tvDescription, tvDuration, tvPrize;
        Button btnStart;

        public ChallengeViewHolder(View itemView) {
            super(itemView);

            btnStart = itemView.findViewById(R.id.btnStart);
            tvDescription = itemView.findViewById(R.id.textViewDescription);
            tvDuration = itemView.findViewById(R.id.textViewDuration);
            tvTitle = itemView.findViewById(R.id.textViewTitle);
            tvPrize = itemView.findViewById(R.id.textViewPrize);
        }
    }
}
