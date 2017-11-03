package xyz.bnayagrawal.android.icsapp.notice;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import xyz.bnayagrawal.android.icsapp.MainActivity;
import xyz.bnayagrawal.android.icsapp.R;
import xyz.bnayagrawal.android.icsapp.internet.VolleyGet;
import xyz.bnayagrawal.android.icsapp.internet.iVolleyCallback;

public class NoticeFragment extends Fragment implements iVolleyCallback {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private SwipeRefreshLayout SwipeRefreshNotice;
    private LinearLayout noticeLoading;

    private ArrayList<NoticeData> notices;
    private String token,url;
    private VolleyGet volleyGet;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.notice_fragment, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_notice);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        noticeLoading = (LinearLayout) view.findViewById(R.id.notice_loading);

        //custom recycler item animation by wasabeef(github)
        SlideInUpAnimator animator = new SlideInUpAnimator(new OvershootInterpolator(1f));
        animator.setChangeDuration(350);
        animator.setAddDuration(350);
        animator.setRemoveDuration(350);
        recyclerView.setItemAnimator(animator);

        //init var
        notices = new ArrayList<>();
        token = getActivity().getSharedPreferences(getString(R.string.SP_USER_FILE), Context.MODE_PRIVATE).getString("USER_JWT_TOKEN",null);
        url = getString(R.string.url_request_notice_data);
        volleyGet = new VolleyGet(this,getActivity(),url,token);

        adapter = new NoticeRecyclerAdapter(getActivity(), notices);
        recyclerView.setAdapter(adapter);

        //initialize swipe refresh function
        SwipeRefreshNotice = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshNotice);

        SwipeRefreshNotice.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                SwipeRefreshNotice.setRefreshing(true);
                volleyGet.fetchData();
            }
        });

        volleyGet.fetchData();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MainActivity) getActivity()).setToolbar(R.id.notice_toolbar);
    }

    @Override
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
    }

    //custom volley callback methods
    public void onResponseReceived(String response) {

    }

    public void onResponseError(String message) {
        //dummy data
        String[] smalDec = {
                "Celebrating World Ethnic Day. 'Ethnic diversity adds richness to a society.' This sentence comes to life with the celebrations of World Ethnic Day. ",
                "Teachers' Day is a special day for the appreciation of teachers, and may include celebrations to honor them for their special contributions in a particular field area, or the community in general.",
                "The Freshers' Party was a night filled with talent, music, excitement and enthusiasm. Every year on Freshers' Party a boy and a girl from each stream is nominated for the prestigious title of Mr. & Ms. Fresher and for that they have to go through 3 rounds of different competitions."
        };

        noticeLoading.setVisibility(View.GONE);
        SwipeRefreshNotice.setRefreshing(false);
        notices.add(0,new NoticeData("Time table for semester exams", smalDec[0], Calendar.getInstance().getTime(), true, null));
        adapter.notifyItemInserted(0);
        notices.add(0,new NoticeData("Feedback on faculty", smalDec[1], Calendar.getInstance().getTime(), false, null));
        adapter.notifyItemInserted(0);
        notices.add(0,new NoticeData("Ek hafte ke liye college chutti", smalDec[2], Calendar.getInstance().getTime(), false, null));
        adapter.notifyItemInserted(0);
    }
}
