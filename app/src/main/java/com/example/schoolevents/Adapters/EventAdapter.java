package com.example.schoolevents.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.schoolevents.Activities.EventDetailActivity;
import com.example.schoolevents.Models.Event;
import com.example.schoolevents.R;

import java.util.ArrayList;

public class EventAdapter extends BaseAdapter {

    private ArrayList<Event> events;
    private Context context;

    public EventAdapter(ArrayList<Event> events, Context context) {
        this.events = events;
        this.context = context;
    }

    @Override
    public int getCount() {
        return events.size();
    }

    @Override
    public Object getItem(int position) {
        return events.get(position);
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
            view = layoutInflater.inflate(R.layout.event_list_item, null);
        }

        ConstraintLayout containerLayout = view.findViewById(R.id.event_list_item_container);
        ImageView eventPhotoIV = view.findViewById(R.id.event_list_item_photo);
        TextView eventNameTV = view.findViewById(R.id.event_list_item_name);
        TextView eventKeysTV = view.findViewById(R.id.event_list_item_key);
        TextView eventDateTV = view.findViewById(R.id.event_list_item_date);
        TextView eventClubNameTV = view.findViewById(R.id.event_list_item_club_name);

        if(position % 2 == 1) {
            if(position % 3 == 0) {
                eventPhotoIV.setImageResource(R.drawable.event3);
            } else {
                eventPhotoIV.setImageResource(R.drawable.event1);
            }
        } else {
            eventPhotoIV.setImageResource(R.drawable.event2);
        }
        eventNameTV.setText(events.get(position).getName());
        eventKeysTV.setText(events.get(position).getKeys());
        eventDateTV.setText(events.get(position).getDate());
        if(position % 2 == 1) {
            if(position % 3 == 0) {
                eventClubNameTV.setText("SauSiber");
            } else {
                eventClubNameTV.setText("Lorem");
            }
        } else {
            eventClubNameTV.setText("İpsum Dolor");
        }

        containerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detailIntent = new Intent(context, EventDetailActivity.class);
                context.startActivity(detailIntent);
            }
        });

        return view;
    }
}
