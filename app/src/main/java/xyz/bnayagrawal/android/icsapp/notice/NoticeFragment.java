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
import android.util.Log;
import android.view.LayoutInflater;
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
import java.util.HashMap;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import xyz.bnayagrawal.android.icsapp.MainActivity;
import xyz.bnayagrawal.android.icsapp.R;
import xyz.bnayagrawal.android.icsapp.event.EventData;
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

        setRetainInstance(true);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MainActivity) getActivity()).setToolbar(R.id.notice_toolbar);
        SwipeRefreshNotice.setEnabled(false);
        volleyGet.fetchData();
    }

    @Override
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
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
                clearNotices();
                JSONObject jData = jsonObject.getJSONObject("data");
                JSONArray jNotices = jData.getJSONArray("notices");

                int id;
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                String title,text,created_at,updated_at;

                JSONArray jObjectArray;
                JSONObject jNotice,jObject;

                HashMap<String,String> users;

                for(int i=0; i < jNotices.length();i++){
                    jNotice = jNotices.getJSONObject(i);
                    id = jNotice.getInt("id");
                    title= jNotice.getString("title");
                    text = jNotice.getString("text");
                    created_at = jNotice.getString("createdAt");
                    updated_at = jNotice.getString("updatedAt");

                    jObjectArray = jNotice.getJSONArray("read");
                    users = new HashMap<>(jObjectArray.length());
                    for(int j=0; j < jObjectArray.length();j++) {
                        jObject = jObjectArray.getJSONObject(j);
                        users.put("id",jObject.getString("id"));
                        users.put("first_name",jObject.getString("firstName"));
                        users.put("last_name",jObject.getString("lastName"));
                        users.put("username",jObject.getString("username"));
                        users.put("email",jObject.getString("email"));
                        users.put("created_at",jObject.getString("createdAt"));
                        users.put("updated_at",jObject.getString("updatedAt"));
                    }

                    notices.add(new NoticeData(
                            id,title,text,
                            dateFormat.parse(created_at),
                            dateFormat.parse(updated_at),
                            users));
                    adapter.notifyItemInserted(notices.size()-1);
                }
            }
        }
        catch (Exception ex) {
            Toast.makeText(getActivity(),"Error parsing data!",Toast.LENGTH_SHORT).show();
        }
        finally {
            noticeLoading.setVisibility(View.GONE);
            SwipeRefreshNotice.setEnabled(true);
            SwipeRefreshNotice.setRefreshing(false);
        }
    }

    public void onResponseError(String message) {
        //dummy data
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
        noticeLoading.setVisibility(View.GONE);
        SwipeRefreshNotice.setRefreshing(false);
        SwipeRefreshNotice.setEnabled(true);
    }

    public void clearNotices() {
        int size = notices.size();
        if (size > 0) {
            notices.clear();
            adapter.notifyItemRangeRemoved(0, size);
        }
    }
}
