package com.example.schoolevents.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.schoolevents.Activities.SchoolClubDetailActivity;
import com.example.schoolevents.Models.SchoolClub;
import com.example.schoolevents.R;

import java.util.ArrayList;

public class SchoolClubsAdapter extends BaseAdapter {

    private ArrayList<SchoolClub> schoolClubs;
    private Context context;

    public SchoolClubsAdapter(ArrayList<SchoolClub> schoolClubs, Context context) {
        this.schoolClubs = schoolClubs;
        this.context = context;
    }

    @Override
    public int getCount() {
        return schoolClubs.size();
    }

    @Override
    public Object getItem(int position) {
        return schoolClubs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            view = layoutInflater.inflate(R.layout.school_list_item, null);
        }

        CardView schoolListItemContainer = view.findViewById(R.id.school_list_item_container);
        ImageView profilePhotoIV = view.findViewById(R.id.school_list_item_pp);
        TextView schoolNameTV = view.findViewById(R.id.school_list_item_name);
        TextView schoolDescTV = view.findViewById(R.id.school_list_item_desc);

        profilePhotoIV.setImageResource(R.drawable.logo);
        schoolNameTV.setText(schoolClubs.get(position).getName());
        schoolDescTV.setText(schoolClubs.get(position).getDescription());
        schoolListItemContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent clubIntent = new Intent(context, SchoolClubDetailActivity.class);
                context.startActivity(clubIntent);
            }
        });

        return view;
    }
}
