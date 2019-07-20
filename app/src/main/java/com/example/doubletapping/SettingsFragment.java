package com.example.doubletapping;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

abstract public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_settings);
    }
}
