package xyz.bnayagrawal.android.icsapp.event;

import android.app.SearchManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import xyz.bnayagrawal.android.icsapp.R;

public class EventSearchResultActivity extends AppCompatActivity {

    private String query_string;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_search_result);

        Toolbar toolbar = (Toolbar) findViewById(R.id.search_event_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SearchView searchView = (SearchView) findViewById(R.id.search_event_searchView);
        searchView.setIconified(false);
        searchView.clearFocus();

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            query_string = intent.getStringExtra(SearchManager.QUERY);
            searchView.setQuery(query_string, false);
            //Perform search
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

