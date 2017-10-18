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
import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import xyz.bnayagrawal.android.icsapp.R;
import xyz.bnayagrawal.android.icsapp.event.adapter.EventRecyclerAdapter;

/**
 * Created by binay on 10/9/2017.
 */

public class TabFragmentEvent extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private SwipeRefreshLayout SwipeRefreshEvents;

    public TabFragmentEvent() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.tab_fragment_event, container, false);

        //Toolbar settings
        setHasOptionsMenu(true);

        //initialize swipe refresh function
        SwipeRefreshEvents = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshEvents);
        SwipeRefreshEvents.setColorSchemeColors(Color.RED,Color.GREEN,Color.BLUE);

        SwipeRefreshEvents.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                SwipeRefreshEvents.setRefreshing(true);
                // TODO: refreshing while fetching the data
                SwipeRefreshEvents.setRefreshing(false);
            }
        });

        //Initializing the RecyclerView Component
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_events);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        //Some dummy event data
        List<EventData> ed = new ArrayList<>();

        //Dummy description
        String[] smalDec = {
                "Celebrating World Ethnic Day. 'Ethnic diversity adds richness to a society.' This sentence comes to life with the celebrations of World Ethnic Day. ",
                "Teachers' Day is a special day for the appreciation of teachers, and may include celebrations to honor them for their special contributions in a particular field area, or the community in general.",
                "The Freshers' Party was a night filled with talent, music, excitement and enthusiasm. Every year on Freshers' Party a boy and a girl from each stream is nominated for the prestigious title of Mr. & Ms. Fresher and for that they have to go through 3 rounds of different competitions."
        };

        String default_image = String.valueOf(R.drawable.image_event_default);

        ed.add(new EventData("Ethnic Day",default_image,smalDec[0],Calendar.getInstance().getTime(),56,18, Calendar.getInstance().getTime(),"Karkala"));
        ed.add(new EventData("Teachers Day",default_image,smalDec[1],Calendar.getInstance().getTime(),32,22,Calendar.getInstance().getTime(),"Tirthali"));
        ed.add(new EventData("Freshers Party",default_image,smalDec[2],Calendar.getInstance().getTime(),16,12,Calendar.getInstance().getTime(),"Padubidri"));

        adapter = new EventRecyclerAdapter(getActivity(),ed);

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
        Toast.makeText(getContext(),"Menu click etab ",Toast.LENGTH_SHORT).show();
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
