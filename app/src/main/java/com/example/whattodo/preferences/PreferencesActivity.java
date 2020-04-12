package com.example.whattodo.preferences;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.whattodo.R;
import com.example.whattodo.preferences.PreferencesFragment;

public class PreferencesActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shared_prefs);
        Log.i("PreferenceActivity", "onCreate");
        getFragmentManager().beginTransaction().replace(android.R.id.content,new PreferencesFragment()).commit();

    }
}
