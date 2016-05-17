package com.zscseh93.settings;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.zscseh93.R;

/**
 * Created by zscse on 2016. 05. 16..
 */
public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
    }
}
