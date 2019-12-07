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
import android.widget.Toast;

import com.example.schoolevents.Activities.EventDetailActivity;
import com.example.schoolevents.Models.Event;
import com.example.schoolevents.R;
import com.nostra13.universalimageloader.core.ImageLoader;

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
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            view = layoutInflater.inflate(R.layout.event_list_item, null);
        }

        ConstraintLayout containerLayout = view.findViewById(R.id.event_list_item_container);
        ImageView eventPhotoIV = view.findViewById(R.id.event_list_item_photo);
        TextView eventNameTV = view.findViewById(R.id.event_list_item_name);
        TextView eventInstructorName = view.findViewById(R.id.event_instructor_name);
        TextView eventDateTV = view.findViewById(R.id.event_list_item_date);
        TextView eventClubNameTV = view.findViewById(R.id.event_list_item_club_name);

        ImageLoader.getInstance().displayImage(events.get(position).getThumbnailUrl(), eventPhotoIV);
        eventNameTV.setText(events.get(position).getName());
        eventInstructorName.setText(events.get(position).getInstructorName());
        eventDateTV.setText(events.get(position).getDate());
        eventClubNameTV.setText(events.get(position).getClub().getName());

        containerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detailIntent = new Intent(context, EventDetailActivity.class);
                detailIntent.putExtra("event_id", events.get(position).getId());
                context.startActivity(detailIntent);
            }
        });

        return view;
    }
}
