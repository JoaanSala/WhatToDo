package com.example.whattodo.Client

import com.example.whattodo.BackendUrl
import com.google.gson.Gson
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

class ApiClient {
    private val httpClient = OkHttpClient()

    fun createPaymentIntent(amount: Double, title: String, event: String, localitzacio: String, completion: (paymentIntentClientSecret: String?, error: String?) -> Unit)
    {
        var integer = Math.round(amount*100); // gives 995

        val mediaType = "application/json; charset=utf-8".toMediaType()

        val payMap: MutableMap<String, Any> = HashMap()
        val itemMap: MutableMap<String, Any> = HashMap()
        val itemList: MutableList<Map<String, Any>> = ArrayList()

        payMap["currency"] = "eur"
        itemMap["id"] = "$title - $event, $localitzacio"
        itemMap["amount"] = integer
        itemList.add(itemMap)
        payMap["items"] = itemList

        var requestJson = Gson().toJson(payMap)

        val body = requestJson.toRequestBody(mediaType)
        val request = Request.Builder().url(BackendUrl + "create-payment-intent").post(body).build()
        httpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                completion(null, "$e")
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    completion(null, "$response")
                } else {
                    val responseData = response.body?.string()
                    val responseJson = responseData?.let { JSONObject(it) } ?: JSONObject()
                    // The response from the server contains the PaymentIntent's client_secret
                    var paymentIntentClientSecret: String = responseJson.getString("clientSecret")
                    completion(paymentIntentClientSecret, null)
                }
            }
        })
    }
}