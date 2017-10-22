package xyz.bnayagrawal.android.icsapp.event;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import xyz.bnayagrawal.android.icsapp.R;

public class EventSearchResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_search_result);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
