package com.example.schoolevents.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.schoolevents.Adapters.EventAdapter;
import com.example.schoolevents.Helper.AppHelper;
import com.example.schoolevents.Helper.VolleySingleton;
import com.example.schoolevents.Models.Event;
import com.example.schoolevents.R;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class EventFragment extends Fragment {

    private static final String TAG = "EventFragment";

    private ArrayList<Event> events;
    private ListView eventListView;
    private EventAdapter eventAdapter;
    private TextView noEventText;
    private ProgressBar progressBar;

    public EventFragment() {
        events = new ArrayList<>();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event, container, false);
        initFragment(view);

        setUpWithAdapter();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getEvents();
    }

    private void getEvents() {
        progressBar.setVisibility(View.VISIBLE);
        events.clear();
        String url = AppHelper.getServerUrl() + "/events/list";
        RequestQueue requestQueue = VolleySingleton.getInstance(getContext()).getRequestQueue();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                progressBar.setVisibility(View.GONE);
                if(response.length() > 0) {
                    noEventText.setVisibility(View.GONE);
                    for(int i = 0 ; i < response.length(); i++) {
                        try {
                            Event event = new Gson().fromJson(response.getJSONObject(i).toString(), Event.class);
                            events.add(event);
                            eventAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    noEventText.setVisibility(View.VISIBLE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                noEventText.setVisibility(View.VISIBLE);
                Toast.makeText(getContext(), "Bir hata oluştu. Lütfen daha sonra tekrar deneyiniz.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onErrorResponse: error -> " + error.toString());
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    private void setUpWithAdapter() {
        eventAdapter = new EventAdapter(events, getContext());
        eventListView.setAdapter(eventAdapter);
    }

    private void initFragment(View view) {
        eventListView = view.findViewById(R.id.event_list_view);
        noEventText = view.findViewById(R.id.no_event_text);
        progressBar = view.findViewById(R.id.event_progress_bar);
    }

}
