package com.example.whattodo.preferences;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;

import androidx.fragment.app.Fragment;

import com.example.whattodo.ActivityCreditCard;
import com.example.whattodo.CreditCardFragment;
import com.example.whattodo.EventFragment;
import com.example.whattodo.LoadingFragment;
import com.example.whattodo.MainActivity;
import com.example.whattodo.R;
import com.google.firebase.auth.FirebaseAuth;

public class PreferencesFragment extends PreferenceFragment {

    FirebaseAuth mAuth;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        /*
        Preference prefsSession = (Preference) findPreference("disableSession");
        prefsSession.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                getActivity().finish();
                return true;
            }
        });

        Preference prefsCreditCard = (Preference) findPreference("creditcard");
        prefsCreditCard.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                startActivity(new Intent(getActivity(), ActivityCreditCard.class));
                return true;
            }
        });
        */
    }
}