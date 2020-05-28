package com.example.whattodo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class ItemEvent_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventitem);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_eventitem, new ItemEvent_Fragment()).commit();
    }
}
