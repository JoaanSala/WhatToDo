package com.example.whattodo

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class LoadingFragment : Fragment() {
    private val mHandler = Handler()
    var mView: View? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_loading, container, false)
        mHandler.postDelayed(object : Runanble() {
            override fun run() {
                fragmentManager!!.beginTransaction().replace(R.id.fragment_main, LogInFragment()).commit()
            }
        }, TIME_OUT.toLong()) // 3,5 seconds
        return mView
    }

    private open inner class Runanble : Runnable {
        override fun run() {}
    }

    companion object {
        private const val TIME_OUT = 3500
    }
}