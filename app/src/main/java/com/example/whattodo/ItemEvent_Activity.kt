package com.example.whattodo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class ItemEvent_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_eventitem)
        supportFragmentManager.beginTransaction().replace(R.id.fragment_eventitem, ItemEvent_Fragment()).commit()
    }
}