package com.example.whattodo

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var locationActivated = false
    var location: String? = ""
    private var notificationManager: NotificationManagerCompat? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        bottom_navigation.setOnNavigationItemSelectedListener{ menuItem ->
            when(menuItem.itemId){
                R.id.nav_home -> {
                    val fragment : Fragment = HomeFragment()
                    openFragment(fragment)
                    true
                }
                R.id.nav_search -> {
                    val fragment : Fragment = SearchFragment()
                    openFragment(fragment)
                    true
                }
                R.id.nav_sale -> {
                    val fragment : Fragment = OfertFragment()
                    openFragment(fragment)
                    true
                }
                R.id.nav_profile -> {
                    val fragment : Fragment = ProfileFragment()
                    openFragment(fragment)
                    true
                }
                else -> false
            }
        }

        notificationManager = NotificationManagerCompat.from(this)
        supportFragmentManager.beginTransaction().replace(R.id.fragment_main, LoadingFragment()).commit()
    }

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_main, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }


    @JvmName("setLocation1")
    fun setLocation(location: String?) {
        this.location = location
        Log.d("SavedLocation", location!!)
    }

    @JvmName("getLocation1")
    fun getLocation(): String? {
        return location
    }

    fun sendOnMainChannel(v: View?) {
        val title = "Ofertes"
        val message = "S'han afegit noves Ofertes, Fes-li una ullada!!"
        val notification = NotificationCompat.Builder(this, WhatToDo.MAINCHANNEL_ID)
                .setSmallIcon(R.drawable.bar_sale_c)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build()
        notificationManager!!.notify(1, notification)
    }
}