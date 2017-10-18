package xyz.bnayagrawal.android.icsapp.event;


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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import xyz.bnayagrawal.android.icsapp.R;
import xyz.bnayagrawal.android.icsapp.event.adapter.WorkshopRecyclerAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class TabFragmentWorkshop extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private SwipeRefreshLayout SwipeRefreshWorkshops;
    private ArrayList<EventData> ed;

    public TabFragmentWorkshop() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.tab_fragment_event, container, false);

        //Toolbar settings
        setHasOptionsMenu(true);

        //initialize swipe refresh function
        SwipeRefreshWorkshops = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshEvents);
        SwipeRefreshWorkshops.setColorSchemeColors(Color.RED,Color.GREEN,Color.BLUE);

        SwipeRefreshWorkshops.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                SwipeRefreshWorkshops.setRefreshing(true);
                // TODO: refreshing while fetching the data
                // currently adds dummy data
                ed.remove(0);
                adapter.notifyItemRemoved(0);
                ed.add(0,new EventData("Fakathon","","blablablablba",Calendar.getInstance().getTime(),56,18,Calendar.getInstance().getTime(),"Karkala"));
                adapter.notifyItemInserted(0);
                SwipeRefreshWorkshops.setRefreshing(false);
            }
        });

        //Initializing the RecyclerView Component
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_events);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        //Dummy data
        ed = new ArrayList<>();
        String default_image = String.valueOf(R.drawable.image_event_default);
        String desc = "Celebrating World Ethnic Day. 'Ethnic diversity adds richness to a society.' This sentence comes to life with the celebrations of World Ethnic Day. ";
        ed.add(new EventData("Android",default_image,"blablablabla", Calendar.getInstance().getTime(),56,18,Calendar.getInstance().getTime(),"Karkala"));
        ed.add(new EventData("Firebase",default_image,"blablablabla",Calendar.getInstance().getTime(),56,18,Calendar.getInstance().getTime(),"Karkala"));

        adapter = new WorkshopRecyclerAdapter(getActivity(),ed);

        //custom recycler item animation by wasabeef(github)
        SlideInUpAnimator animator = new SlideInUpAnimator(new OvershootInterpolator(1f));
        animator.setChangeDuration(500);
        animator.setAddDuration(500);
        animator.setRemoveDuration(500);
        recyclerView.setItemAnimator(animator);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Toast.makeText(getContext(),"Menu click wtab ",Toast.LENGTH_SHORT).show();
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
}
