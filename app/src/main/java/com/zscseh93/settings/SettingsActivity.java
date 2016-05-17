package com.zscseh93.settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import com.zscseh93.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        SettingsFragment settingsFragment = new SettingsFragment();
        getFragmentManager().beginTransaction().replace(R.id.fragmentSettings, settingsFragment)
                .commit();
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isNotificationEnabled = sharedPreferences.getBoolean("pref_notifications", false);

        Intent intent = new Intent();
        intent.putExtra("NOTIFICATION_ENABLED", isNotificationEnabled);
        setResult(RESULT_OK, intent);
        finish();
    }
}
