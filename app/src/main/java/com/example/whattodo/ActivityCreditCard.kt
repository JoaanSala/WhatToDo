package com.example.whattodo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class ActivityCreditCard : AppCompatActivity() {
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_creditcard)
        supportFragmentManager.beginTransaction().replace(R.id.frame_creditcard, CreditCardFragment()).commit()
    }
}