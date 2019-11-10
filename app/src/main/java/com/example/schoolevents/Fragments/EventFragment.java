package com.example.schoolevents.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.schoolevents.Adapters.EventAdapter;
import com.example.schoolevents.Models.Event;
import com.example.schoolevents.R;

import java.util.ArrayList;

public class EventFragment extends Fragment {

    private ArrayList<Event> events;
    private ListView eventListView;

    public EventFragment() {
        // Required empty public constructor
        events = new ArrayList<>();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event, container, false);
        initFragment(view);

        for(int i = 0; i < 10; i++) {
            Event event = new Event();
            event.setDate("11.04.2020 13:00");
            event.setKeys("key1, key2, key3, key4, key5, key6, key7, key8, key9");
            event.setName("Lorem İpsum Dolor Amet");
            events.add(event);
        }

        setUpWithAdapter();

        return view;
    }

    private void setUpWithAdapter() {
        EventAdapter eventAdapter = new EventAdapter(events, getContext());
        eventListView.setAdapter(eventAdapter);
    }

    private void initFragment(View view) {
        eventListView = view.findViewById(R.id.event_list_view);
    }

}
