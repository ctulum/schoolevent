package com.example.schoolevents.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.schoolevents.Activities.EventDetailActivity;
import com.example.schoolevents.Models.Event;
import com.example.schoolevents.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class LastEventsRecyclerAdapter extends RecyclerView.Adapter<LastEventsRecyclerAdapter.EventViewHolder> {

    private ArrayList<Event> events;
    private Context context;

    public LastEventsRecyclerAdapter(ArrayList<Event> events, Context context) {
        this.events = events;
        this.context = context;
    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.school_club_last_event_list_item, null);

        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final EventViewHolder holder, final int position) {
        Event event = events.get(position);

        ImageLoader.getInstance().displayImage(events.get(position).getThumbnailUrl(), holder.eventImage);
        holder.eventName.setText(event.getName());

        holder.eventContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detailIntent = new Intent(context, EventDetailActivity.class);
                detailIntent.putExtra("event_id", events.get(holder.getAdapterPosition()).getId());
                context.startActivity(detailIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public class EventViewHolder extends RecyclerView.ViewHolder {

        private ConstraintLayout eventContainer;
        private ImageView eventImage;
        private TextView eventName;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            eventContainer = itemView.findViewById(R.id.last_events_container);
            eventImage = itemView.findViewById(R.id.event_image);
            eventName = itemView.findViewById(R.id.event_name);
        }
    }
}
