package xyz.bnayagrawal.android.icsapp.settings;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import xyz.bnayagrawal.android.icsapp.R;

/**
 * Created by binay on 10/22/2017.
 */

public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
