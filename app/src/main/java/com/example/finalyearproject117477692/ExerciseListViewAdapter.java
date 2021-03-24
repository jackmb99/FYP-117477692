package com.example.finalyearproject117477692;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class ExerciseListViewAdapter extends BaseAdapter {
    private Activity activity;
    private List<Exercise> listExercise;

    public ExerciseListViewAdapter(Activity activity, List<Exercise> listExercise){
        this.activity = activity;
        this.listExercise = listExercise;
    }

    @Override
    public int getCount() {
        return listExercise.size();
    }

    @Override
    public Object getItem(int position) {
        return listExercise.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    // displaying the data in list
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ExerciseListViewAdapter.ViewHolder holder;
        if(convertView == null){
            convertView = activity.getLayoutInflater().inflate(R.layout.exerciserow_listview, null);

            holder = new ExerciseListViewAdapter.ViewHolder();
            holder.tvEmail = convertView.findViewById(R.id.tvEmail);
            holder.tvType = convertView.findViewById(R.id.tvType);
            holder.tvDistanceCovered = convertView.findViewById(R.id.tvDistanceCovered);
            holder.tvComment = convertView.findViewById(R.id.tvComment);
            holder.tvDate = convertView.findViewById(R.id.tvDate);



            convertView.setTag(holder);
        }else{
            holder = (ExerciseListViewAdapter.ViewHolder)convertView.getTag();
        }

        holder.tvEmail.setText(listExercise.get(position).getEmail());
        holder.tvType.setText(listExercise.get(position).getExerciseType());
        holder.tvDistanceCovered.setText(listExercise.get(position).getDistanceCovered() + "km");
        holder.tvComment.setText(listExercise.get(position).getComment());
        holder.tvDate.setText(listExercise.get(position).getDate());


        return convertView;
    }

    // defining the items in the holder
    class ViewHolder{
        TextView tvEmail;
        TextView tvType;
        TextView tvDistanceCovered;
        TextView tvDate;
        TextView tvComment;
    }
}
