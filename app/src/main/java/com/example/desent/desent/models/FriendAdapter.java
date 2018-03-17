package com.example.desent.desent.models;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.desent.desent.R;

import java.util.List;

/**
 * Created by ragnhildlarsen on 17.03.2018.
 */

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendViewHolder>{

    //this context will be used to inflate the layout
    private Context context;
    //storing all the friends in a list
    private List<Friend> friendList;

    //getting the context and friend list with constructor
    public FriendAdapter(Context context, List<Friend> friendList){
        this.context = context;
        this.friendList = friendList;
    }
    
    @Override
    public FriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_friends, null);
        return new FriendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FriendViewHolder holder, int position) {
        //getting the friend of the specified position
        Friend friend = friendList.get(position);

        //binding the data with the viewholder views
        holder.tvName.setText(friend.getName());
        holder.tvNumCoins.setText(String.valueOf(friend.getNum_coins()));
        Resources resources = context.getResources();
        holder.tvAvgCf.setText(String.valueOf(friend.getAvg_cf()) + " " + resources.getString(R.string.carbon_footprint_unit));

        holder.image.setImageDrawable(context.getResources().getDrawable(friend.getImage()));
    }

    @Override
    public int getItemCount() {
        return friendList.size();
    }

    public class FriendViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvNumCoins, tvAvgCf;
        ImageView image;

        public FriendViewHolder(View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.textViewName);
            tvNumCoins = itemView.findViewById(R.id.textViewNumCoins);
            tvAvgCf = itemView.findViewById(R.id.textViewAvgCf);
            image = itemView.findViewById(R.id.imageView);
        }
    }
}
