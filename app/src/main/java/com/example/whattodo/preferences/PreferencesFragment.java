package com.example.whattodo.preferences;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;

import com.example.whattodo.LoadingFragment;
import com.example.whattodo.MainActivity;
import com.example.whattodo.R;
import com.google.firebase.auth.FirebaseAuth;

public class PreferencesFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        Preference myPref = (Preference) findPreference("disableSession");
        myPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                MainActivity main = new MainActivity();
                main.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_main, new LoadingFragment()).commit();
                FirebaseAuth.getInstance().signOut();
                getActivity().finish();
                return true;
            }
        });
    }
}