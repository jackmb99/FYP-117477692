package com.example.finalyearproject117477692;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

// leaderboard functionality from https://www.youtube.com/watch?v=j1_tEaYchyk

public class DistanceAdapter extends RecyclerView.Adapter<DistanceAdapter.DistanceViewAdapter> {

    // declare variables
    private static final String TAG = "****Log check****";

    List<DistanceData> list;
    Context context;
    int i = 1;

    public DistanceAdapter(List<DistanceData> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public class DistanceViewAdapter extends RecyclerView.ViewHolder{
        // initialising variables
        TextView name,distance,rank;

        public DistanceViewAdapter(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvUsername);
            distance = itemView.findViewById(R.id.tvDistance);
            rank = itemView.findViewById(R.id.tvRank);


        }
    }

    @NonNull
    @Override
    public DistanceViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // assigning to the specific layout xml file
        View view = LayoutInflater.from(context).inflate(R.layout.score_list_item, parent, false);
        return new DistanceViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DistanceViewAdapter holder, int position) {

        // variables assigned to text fields in the layout xml file
        DistanceData currentItem = list.get(position);
        holder.name.setText(currentItem.getName());
        holder.distance.setText(String.valueOf(currentItem.getDistance() + "km")); // making km appear in text view
        holder.rank.setText(String.valueOf(list.size() - position));
        i++; // increasing rank, rank starts at 1 as defined above
    }
        // avoid null values on display
    @Override
    public int getItemCount() {
        return list.size();
    }
}
