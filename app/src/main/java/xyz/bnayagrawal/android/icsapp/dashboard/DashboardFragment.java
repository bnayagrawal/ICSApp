package xyz.bnayagrawal.android.icsapp.dashboard;

/**
 * Created by root on 29/9/17.
 */

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import xyz.bnayagrawal.android.icsapp.MainActivity;
import xyz.bnayagrawal.android.icsapp.R;

public class DashboardFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        ((MainActivity)getActivity()).setToolbar();
    }

    @Override
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        Toast.makeText(getContext(),"Config change detected!!",Toast.LENGTH_SHORT).show();
    }
}