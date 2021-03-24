package com.example.finalyearproject117477692;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

// Code from Michael Gleesons CRUD on firebase

public class ListViewAdapter extends BaseAdapter {
    private Activity activity;
    private List<Member> listPerson;

    public ListViewAdapter(Activity activity, List<Member> listPerson){
        this.activity = activity;
        this.listPerson = listPerson;
    }

    @Override
    public int getCount() {
        return listPerson.size();
    }

    @Override
    public Object getItem(int position) {
        return listPerson.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            convertView = activity.getLayoutInflater().inflate(R.layout.row_listview, null);
            // ViewHolder class which caches views associated with the default Preference layouts.
            holder = new ViewHolder();
            holder.textViewFirstName = convertView.findViewById(R.id.textViewFirstName);
         //   holder.textViewLastName = convertView.findViewById(R.id.textViewLastName);
            holder.textViewAge = convertView.findViewById(R.id.textViewAge);
            holder.tvDate = convertView.findViewById(R.id.tvDate);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }

        holder.textViewFirstName.setText(listPerson.get(position).getName());
        holder.textViewAge.setText(listPerson.get(position).getContact() + "km");
        holder.tvDate.setText(listPerson.get(position).getDateStamp());



        return convertView;
    }

// defining the items in the holder
    class ViewHolder{
        TextView textViewFirstName;
        TextView textViewAge;
        TextView tvDate;
    }
}
