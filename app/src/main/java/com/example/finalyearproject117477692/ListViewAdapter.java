package com.example.finalyearproject117477692;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Michael Gleeson on 27/02/2019
 * Copyright (c) 2019 | gleeson.io
 */
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
            holder.textViewLastName = convertView.findViewById(R.id.textViewLastName);
            holder.textViewAge = convertView.findViewById(R.id.textViewAge);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }

        holder.textViewFirstName.setText(listPerson.get(position).getName());
        holder.textViewLastName.setText(listPerson.get(position).getContact());
        holder.textViewAge.setText(listPerson.get(position).getAge() + "");

        return convertView;
    }

    class ViewHolder{
        TextView textViewFirstName;
        TextView textViewLastName;
        TextView textViewAge;
    }
}
