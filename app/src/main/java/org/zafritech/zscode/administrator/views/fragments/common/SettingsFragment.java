package org.zafritech.zscode.administrator.views.fragments.common;


import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import org.zafritech.zscode.administrator.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    public static SettingsFragment newInstance()
    {
        return new SettingsFragment();
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

        setPreferencesFromResource(R.xml.root_preferences, rootKey);
    }
}
