package xyz.bnayagrawal.android.icsapp.notice;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;

import java.util.ArrayList;
import java.util.Calendar;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import xyz.bnayagrawal.android.icsapp.MainActivity;
import xyz.bnayagrawal.android.icsapp.R;

public class NoticeFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private SwipeRefreshLayout SwipeRefreshNotice;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.notice_fragment, container, false);

        //initialize swipe refresh function
        SwipeRefreshNotice = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshNotice);
        SwipeRefreshNotice.setColorSchemeColors(Color.RED,Color.GREEN,Color.BLUE);

        SwipeRefreshNotice.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                SwipeRefreshNotice.setRefreshing(true);
                // TODO: refreshing while fetching the data
                SwipeRefreshNotice.setRefreshing(false);
            }
        });

        //Initializing the RecyclerView Component
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_notice);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        //custom recycler item animation by wasabeef(github)
        SlideInUpAnimator animator = new SlideInUpAnimator(new OvershootInterpolator(1f));
        animator.setChangeDuration(300);
        animator.setAddDuration(300);
        animator.setRemoveDuration(300);
        recyclerView.setItemAnimator(animator);

        //Some dummy notice data
        ArrayList<NoticeData> nd = new ArrayList<>();

        //Dummy description
        String[] smalDec = {
                "Celebrating World Ethnic Day. 'Ethnic diversity adds richness to a society.' This sentence comes to life with the celebrations of World Ethnic Day. ",
                "Teachers' Day is a special day for the appreciation of teachers, and may include celebrations to honor them for their special contributions in a particular field area, or the community in general.",
                "The Freshers' Party was a night filled with talent, music, excitement and enthusiasm. Every year on Freshers' Party a boy and a girl from each stream is nominated for the prestigious title of Mr. & Ms. Fresher and for that they have to go through 3 rounds of different competitions."
        };

        nd.add(new NoticeData("Time table for semester exams",smalDec[0],Calendar.getInstance().getTime(),true,null));
        nd.add(new NoticeData("Feedback on faculty",smalDec[1],Calendar.getInstance().getTime(),false,null));
        nd.add(new NoticeData("Ek hafte ke liye college chutti",smalDec[2],Calendar.getInstance().getTime(),false,null));

        adapter = new NoticeRecyclerAdapter(getActivity(),nd);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        ((MainActivity)getActivity()).setToolbar(R.id.notice_toolbar);
    }

    @Override
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
    }
}
