package xyz.bnayagrawal.android.icsapp.event;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import xyz.bnayagrawal.android.icsapp.R;
import xyz.bnayagrawal.android.icsapp.event.adapter.WorkshopRecyclerAdapter;
import xyz.bnayagrawal.android.icsapp.internet.VolleyGet;
import xyz.bnayagrawal.android.icsapp.internet.iVolleyCallback;


/**
 * A simple {@link Fragment} subclass.
 */
public class TabFragmentWorkshop extends Fragment implements iVolleyCallback {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private SwipeRefreshLayout SwipeRefreshWorkshops;
    private LinearLayout eventLoading;

    private ArrayList<EventData> events;
    private String token,url;
    private String default_image;
    private VolleyGet volleyGet;

    public TabFragmentWorkshop() {
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
        animator.setChangeDuration(500);
        animator.setAddDuration(500);
        animator.setRemoveDuration(500);
        recyclerView.setItemAnimator(animator);

        //init var
        events = new ArrayList<>();
        token = getActivity().getSharedPreferences(getString(R.string.SP_USER_FILE), Context.MODE_PRIVATE).getString("USER_JWT_TOKEN",null);
        url = getString(R.string.url_request_event_data);
        default_image = String.valueOf(R.drawable.image_event_default);
        volleyGet = new VolleyGet(this,getActivity(),url,token);

        adapter = new WorkshopRecyclerAdapter(getActivity(),events);
        recyclerView.setAdapter(adapter);

        //initialize swipe refresh function
        SwipeRefreshWorkshops = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshEvents);

        SwipeRefreshWorkshops.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                SwipeRefreshWorkshops.setRefreshing(true);
                volleyGet.fetchData();
            }
        });

        setRetainInstance(true);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        SwipeRefreshWorkshops.setEnabled(false);
        volleyGet.fetchData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Toast.makeText(getActivity(),"Menu click wtab ",Toast.LENGTH_SHORT).show();
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
        SwipeRefreshWorkshops.setEnabled(true);
    }

    public void onResponseError(String message) {
        //dummy data
        /*String[] smalDec = {
                "Celebrating World Ethnic Day. 'Ethnic diversity adds richness to a society.' This sentence comes to life with the celebrations of World Ethnic Day. ",
                "Teachers' Day is a special day for the appreciation of teachers, and may include celebrations to honor them for their special contributions in a particular field area, or the community in general.",
                "The Freshers' Party was a night filled with talent, music, excitement and enthusiasm. Every year on Freshers' Party a boy and a girl from each stream is nominated for the prestigious title of Mr. & Ms. Fresher and for that they have to go through 3 rounds of different competitions."
        };

        eventLoading.setVisibility(View.GONE);
        SwipeRefreshWorkshops.setRefreshing(false);
        events.add(new EventData("Android",default_image,"blablablabla", Calendar.getInstance().getTime(),56,18,Calendar.getInstance().getTime(),"Karkala"));
        events.add(new EventData("Firebase",default_image,"blablablabla",Calendar.getInstance().getTime(),56,18,Calendar.getInstance().getTime(),"Karkala"));
        adapter.notifyDataSetChanged();*/
        SwipeRefreshWorkshops.setEnabled(true);
    }
}
