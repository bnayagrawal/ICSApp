package xyz.bnayagrawal.android.icsapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import xyz.bnayagrawal.android.icsapp.settings.SettingsFragment;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }
}
