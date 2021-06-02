package com.example.whattodo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class ActivityComent : AppCompatActivity() {
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coment)
        supportFragmentManager.beginTransaction().replace(R.id.frame_coment, ComentFragment()).commit()
    }
}