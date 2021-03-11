package com.example.finalyearproject117477692;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

// Code from Michael Gleesons CRUD on firebase

public class GoalListViewAdapter extends BaseAdapter {
    private Activity activity;
    private List<Goal> listGoal;

    public GoalListViewAdapter(Activity activity, List<Goal> listGoal){
        this.activity = activity;
        this.listGoal = listGoal;
    }

    @Override
    public int getCount() {
        return listGoal.size();
    }

    @Override
    public Object getItem(int position) {
        return listGoal.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    // displaying the data in list
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GoalListViewAdapter.ViewHolder holder;
        if(convertView == null){
            convertView = activity.getLayoutInflater().inflate(R.layout.goalrow_listview, null);

            holder = new GoalListViewAdapter.ViewHolder();
            holder.tvTitle = convertView.findViewById(R.id.tvTitle);
            holder.tvDescription = convertView.findViewById(R.id.tvDescription);
            holder.tvDate = convertView.findViewById(R.id.tvDate);


            convertView.setTag(holder);
        }else{
            holder = (GoalListViewAdapter.ViewHolder)convertView.getTag();
        }

        holder.tvTitle.setText(listGoal.get(position).getTitle());
        holder.tvDescription.setText(listGoal.get(position).getDescription());
        holder.tvDate.setText(listGoal.get(position).getDate());


        return convertView;
    }

    // defining the items in the holder
    class ViewHolder{
        TextView tvTitle;
        TextView tvDescription;
        TextView tvDate;
    }
}
