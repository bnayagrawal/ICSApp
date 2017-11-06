package xyz.bnayagrawal.android.icsapp.event;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import xyz.bnayagrawal.android.icsapp.R;
import xyz.bnayagrawal.android.icsapp.event.adapter.EventRecyclerAdapter;
import xyz.bnayagrawal.android.icsapp.internet.VolleyGet;
import xyz.bnayagrawal.android.icsapp.internet.iVolleyCallback;

/**
 * Created by binay on 10/9/2017.
 */

public class TabFragmentEvent extends Fragment implements iVolleyCallback {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private SwipeRefreshLayout SwipeRefreshEvents;
    private LinearLayout eventLoading;

    private ArrayList<EventData> events;
    private String token,url;
    private String default_image;
    private VolleyGet volleyGet;
    private boolean isFetchingEventData;

    public TabFragmentEvent() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.tab_fragment_event, container, false);

        //Toolbar settings
        setHasOptionsMenu(true);

        //Initializing the RecyclerView Component
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_events);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        eventLoading = (LinearLayout) view.findViewById(R.id.event_loading);

        //custom recycler item animation by wasabeef(github)
        SlideInUpAnimator animator = new SlideInUpAnimator(new OvershootInterpolator(1f));
        animator.setChangeDuration(350);
        animator.setAddDuration(350);
        animator.setRemoveDuration(350);
        recyclerView.setItemAnimator(animator);

        //init var
        events = new ArrayList<>();
        token = getActivity().getSharedPreferences(getString(R.string.SP_USER_FILE), Context.MODE_PRIVATE).getString("USER_JWT_TOKEN",null);
        url = getString(R.string.url_request_event_data);
        default_image = String.valueOf(R.drawable.image_event_default);
        volleyGet = new VolleyGet(this,getActivity(),url,token);

        adapter = new EventRecyclerAdapter(getActivity(),events);
        recyclerView.setAdapter(adapter);

        //initialize swipe refresh function
        SwipeRefreshEvents = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshEvents);

        SwipeRefreshEvents.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                SwipeRefreshEvents.setRefreshing(true);
                volleyGet.fetchData();
            }
        });

        setRetainInstance(true);
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("IS_FETCHING_EVENT_DATA",isFetchingEventData);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        SwipeRefreshEvents.setEnabled(false);
        volleyGet.fetchData();
        if (savedInstanceState != null) {

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Toast.makeText(getActivity(),"Menu click etab ",Toast.LENGTH_SHORT).show();
        switch (item.getItemId()) {
            case R.id.event_filter_show_all:
                if (item.isChecked())
                    item.setChecked(false);
                else
                    item.setChecked(true);
                return true;
            case R.id.event_filter_interested:
                if (item.isChecked())
                    item.setChecked(false);
                else
                    item.setChecked(true);
                return true;
            case R.id.event_filter_going:
                if (item.isChecked())
                    item.setChecked(false);
                else
                    item.setChecked(true);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //custom volley callback methods
    public void onResponseReceived(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            String status = jsonObject.getString("status");
            if(status.equals("error")){
                Toast.makeText(getActivity(),"Error",Toast.LENGTH_SHORT).show();
            }
            else {
                events.clear();
                JSONObject jData = jsonObject.getJSONObject("data");
                JSONArray jEvents = jData.getJSONArray("events");

                int id;
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                String title,description,phone,image_url,start_time,end_time,venue,created_at,updated_at;

                JSONArray jObjectArray;
                JSONObject jEvent,jObject;

                HashMap<String,String> user;
                ArrayList<HashMap<String,String>> users_starred,users_attending,users_attended;

                for(int i=0; i < jEvents.length();i++){
                    jEvent = jEvents.getJSONObject(i);
                    id = jEvent.getInt("id");
                    title= jEvent.getString("title");
                    description = jEvent.getString("description");
                    image_url = getResources().getString(R.string.url_server_address) + jEvent.getString("imageUrl");
                    start_time = jEvent.getString("startTime");
                    end_time = jEvent.getString("endTime");
                    venue = jEvent.getString("venue");
                    created_at = jEvent.getString("createdAt");
                    updated_at = jEvent.getString("updatedAt");
                    phone = jEvent.getString("phone");

                    //starred users
                    jObjectArray = jEvent.getJSONArray("starred");
                    users_starred = new ArrayList<>();
                    for(int j=0; j < jObjectArray.length(); j++) {
                        jObject = jObjectArray.getJSONObject(j);
                        user = new HashMap<>();
                        user.put("id",jObject.getString("id"));
                        user.put("first_name",jObject.getString("firstName"));
                        user.put("last_name",jObject.getString("lastName"));
                        user.put("username",jObject.getString("username"));
                        user.put("email",jObject.getString("email"));
                        user.put("created_at",jObject.getString("createdAt"));
                        user.put("updated_at",jObject.getString("updatedAt"));
                        users_starred.add(user);
                    }

                    //attended users
                    jObjectArray = jEvent.getJSONArray("attended");
                    users_attended = new ArrayList<>();
                    for(int j=0; j < jObjectArray.length(); j++) {
                        jObject = jObjectArray.getJSONObject(j);
                        user = new HashMap<>();
                        user.put("id",jObject.getString("id"));
                        user.put("first_name",jObject.getString("firstName"));
                        user.put("last_name",jObject.getString("lastName"));
                        user.put("username",jObject.getString("username"));
                        user.put("email",jObject.getString("email"));
                        user.put("created_at",jObject.getString("createdAt"));
                        user.put("updated_at",jObject.getString("updatedAt"));
                        users_attended.add(user);
                    }

                    //attending users
                    jObjectArray = jEvent.getJSONArray("attending");
                    users_attending = new ArrayList<>();
                    for(int j=0; j < jObjectArray.length(); j++) {
                        jObject = jObjectArray.getJSONObject(j);
                        user = new HashMap<>();
                        user.put("id",jObject.getString("id"));
                        user.put("first_name",jObject.getString("firstName"));
                        user.put("last_name",jObject.getString("lastName"));
                        user.put("username",jObject.getString("username"));
                        user.put("email",jObject.getString("email"));
                        user.put("created_at",jObject.getString("createdAt"));
                        user.put("updated_at",jObject.getString("updatedAt"));
                        users_attending.add(user);
                    }

                    events.add(new EventData(
                            id,title,image_url,description,venue,phone,
                            dateFormat.parse(start_time),
                            dateFormat.parse(end_time),
                            dateFormat.parse(created_at),
                            dateFormat.parse(updated_at),
                            users_attending,users_attended,users_starred));
                    adapter.notifyDataSetChanged();
                }
            }
        }
        catch (Exception ex) {
            Toast.makeText(getActivity(),"Error parsing data!",Toast.LENGTH_SHORT).show();
        }
        finally {
            eventLoading.setVisibility(View.GONE);
            isFetchingEventData = false;
            SwipeRefreshEvents.setEnabled(true);
            SwipeRefreshEvents.setRefreshing(false);
        }
    }

    public void onResponseError(String message) {
        Toast.makeText(getActivity(),message + "\n Pull down to refresh again",Toast.LENGTH_SHORT).show();
        eventLoading.setVisibility(View.GONE);
        isFetchingEventData = false;
        SwipeRefreshEvents.setEnabled(true);
        SwipeRefreshEvents.setRefreshing(false);
    }
}
