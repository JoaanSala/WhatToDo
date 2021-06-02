package com.example.whattodo.preferences

import android.os.Bundle
import android.preference.PreferenceFragment
import com.example.whattodo.R
import com.google.firebase.auth.FirebaseAuth

class PreferencesFragment : PreferenceFragment() {
    var mAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.preferences)

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