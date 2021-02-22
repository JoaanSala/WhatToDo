package com.example.whattodo;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class ActivityCreditCard extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creditcard);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_creditcard, new CreditCardFragment()).commit();
    }
}
