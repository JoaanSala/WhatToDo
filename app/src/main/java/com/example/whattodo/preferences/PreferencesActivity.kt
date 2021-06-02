package com.example.whattodo.preferences

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.whattodo.R

class PreferencesActivity : AppCompatActivity() {
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.shared_prefs)
        Log.i("PreferenceActivity", "onCreate")
        fragmentManager.beginTransaction().replace(android.R.id.content, PreferencesFragment()).commit()
    }
}