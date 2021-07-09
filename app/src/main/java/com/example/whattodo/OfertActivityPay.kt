package com.example.whattodo

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.whattodo.Client.ApiClient
import com.stripe.android.PaymentConfiguration
import com.stripe.android.Stripe
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.util.*

//val BackendUrl = "http://10.0.2.2:4242/"
val BackendUrl = "https://backend-stripe-payment.herokuapp.com/"

class OfertActivityPay: AppCompatActivity() {
    private val httpClient = OkHttpClient()
    private lateinit var publishableKey: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pay)

        fetchPublishableKey()

        supportFragmentManager.beginTransaction().replace(R.id.frame_pay, OfertFragmentPay()).commit()
    }


    private fun fetchPublishableKey() {
        val request = Request.Builder()
            .url(BackendUrl + "config")
            .build()
        httpClient.newCall(request)
            .enqueue(object: Callback {
                override fun onFailure(call: Call, e: IOException) {
                   // displayAlert("Failed to load page", "Error: $e")
                }

                override fun onResponse(call: Call, response: Response) {
                    if (!response.isSuccessful) {
                        //displayAlert("Failed to load page","Error: $response")

                    } else {
                        val responseData = response.body?.string()
                        val responseJson =
                            responseData?.let { JSONObject(it) } ?: JSONObject()
                        // For added security, our sample app gets the publishable key
                        // from the server.
                        publishableKey = responseJson.getString("publishableKey")

                        // Set up PaymentConfiguration with your Stripe publishable key
                        PaymentConfiguration.init(applicationContext, publishableKey)
                    }
                }
            })
    }

}