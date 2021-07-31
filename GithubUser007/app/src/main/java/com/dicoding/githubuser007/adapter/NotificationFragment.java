package com.dicoding.githubuser007.adapter;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import com.dicoding.githubuser007.R;
import com.dicoding.githubuser007.notification.NotificationBroadcast;

public class NotificationFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
    private String NOTIFICATION;
    private SwitchPreference switchPreference;
    private NotificationBroadcast notificationBroadcast;

    @Override
    public void onCreatePreferences(Bundle bundle, String string) {
        addPreferencesFromResource(R.xml.preferences);
        notificationBroadcast = new NotificationBroadcast();
        init();
        setSummaries();
    }

    private void init(){
        NOTIFICATION = getResources().getString(R.string.notification);
        switchPreference =  findPreference(NOTIFICATION);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals(NOTIFICATION)){
            switchPreference.setChecked(sharedPreferences.getBoolean(NOTIFICATION, false));
        }
    }


    private void setSummaries(){
        SharedPreferences sh = getPreferenceManager().getSharedPreferences();
        switchPreference.setChecked(sh.getBoolean(NOTIFICATION, false));
        switchPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean isChecked = (boolean) newValue;
                if (isChecked){
                    notificationBroadcast.setNotification(getActivity(), NotificationBroadcast.TYPE_REPEATING, "09:00", getString(R.string.notification_text));
                }
                else {
                    notificationBroadcast.cancelNotification(getActivity(), NotificationBroadcast.TYPE_REPEATING);
                } return true;
            }
        });
    }
}