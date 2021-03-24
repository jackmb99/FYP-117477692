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

public class GroupDistanceAdapter extends RecyclerView.Adapter<GroupDistanceAdapter.GroupDistanceViewAdapter> {

    // declare variables
    private static final String TAG = "****Log check****";

    List<DistanceData> list;
    Context context;
    int i = 1;

    public GroupDistanceAdapter(List<DistanceData> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public class GroupDistanceViewAdapter extends RecyclerView.ViewHolder{
        // initialising variables
        TextView name,distance,rank;

        public GroupDistanceViewAdapter(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvUsername2);
            distance = itemView.findViewById(R.id.tvDistance2);
            rank = itemView.findViewById(R.id.tvRank2);


        }
    }

    @NonNull
    @Override
    public GroupDistanceViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // assigning to the specific layout xml file
        View view = LayoutInflater.from(context).inflate(R.layout.groupscore_list_item, parent, false);
        return new GroupDistanceViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupDistanceViewAdapter holder, int position) {

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
