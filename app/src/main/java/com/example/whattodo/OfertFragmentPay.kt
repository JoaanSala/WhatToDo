package com.example.whattodo

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.whattodo.Client.ApiClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.GsonBuilder
import com.google.gson.internal.GsonBuildConfig
import com.stripe.android.ApiResultCallback
import com.stripe.android.PaymentConfiguration
import com.stripe.android.PaymentIntentResult
import com.stripe.android.Stripe
import com.stripe.android.model.ConfirmPaymentIntentParams
import com.stripe.android.model.StripeIntent
import com.stripe.android.view.CardInputWidget
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException
import java.lang.ref.WeakReference
import java.util.*

class OfertFragmentPay : Fragment(), View.OnClickListener{

    private var paymentIntentClientSecret: String? = null
    private lateinit var stripe: Stripe

    private lateinit var btnAccept: Button
    private lateinit var btnCancell: Button
    private lateinit var btnFinish: Button
    private lateinit var buttons1st: LinearLayout
    private lateinit var buttons2nd: LinearLayout
    private lateinit var loading_payment: LinearLayout

    private lateinit var title: TextView
    private lateinit var location: TextView
    private lateinit var price: TextView

    private lateinit var mFirestore: FirebaseFirestore
    private lateinit var mAuth: FirebaseAuth
    private lateinit var userID: String

    private lateinit var intent: Intent

    private lateinit var viewOfLayout: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewOfLayout = inflater.inflate(R.layout.fragment_pay, container, false)

        mAuth = FirebaseAuth.getInstance()
        userID = mAuth.currentUser!!.uid
        mFirestore = FirebaseFirestore.getInstance()

        btnAccept = viewOfLayout.findViewById(R.id.btn_accept)
        btnCancell = viewOfLayout.findViewById(R.id.btn_cancell)
        btnFinish = viewOfLayout.findViewById(R.id.btn_finish)
        buttons1st = viewOfLayout.findViewById(R.id.buttonCreditHome)
        buttons2nd = viewOfLayout.findViewById(R.id.buttonCreditFinish)
        loading_payment = viewOfLayout.findViewById(R.id.loading_payment)

        title = viewOfLayout.findViewById(R.id.ofert_title)
        location = viewOfLayout.findViewById(R.id.ofert_location)
        price = viewOfLayout.findViewById(R.id.pay_amount)

        intent = requireActivity().intent
        title.setText(intent.getStringExtra("title"))
        location.setText(intent.getStringExtra("event") + ", " + intent.getStringExtra("localitzacio"))
        price.setText(intent.getStringExtra("price") + " €")

        btnAccept.setOnClickListener(this)
        btnCancell.setOnClickListener(this)
        btnFinish.setOnClickListener(this)

        stripe = Stripe(
            requireContext(),
            Objects.requireNonNull("pk_test_51IfnlWF0WdHSkK4FOuh1tLZoMQFU4XudmUt568HwdP8WiilSkbF2jH8rRodE7whryYt16PD06wZSujQdMp7Bd1LP00BDqekftm")
        )

        startCheckout()

        return viewOfLayout
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_cancell -> requireActivity().finish()
            R.id.btn_finish -> {
                updateUserOfert()
                requireActivity().finish()
            }
        }
    }

    private fun updateUserOfert() {
        val ofertDocument = mFirestore.collection("users").document(userID).collection("PaidOferts").document(intent.getStringExtra("document")!!)

        val TitleOfert = intent.getStringExtra("title")
        val EventOfert = intent.getStringExtra("event")
        val LocalizationOfert = intent.getStringExtra("localitzacio")

        val data: MutableMap<String, Any> = HashMap()

        data["info"] = "$TitleOfert, $EventOfert, $LocalizationOfert"
        data["acquired"] = true

        ofertDocument.set(data).addOnSuccessListener { Log.d("TAG", "onSuccess : ofert Added with Id " + intent.getStringExtra("document")) }
    }

    private fun startCheckout() {
        val amount = intent.getStringExtra("price")!!.replace(",", ".").toDouble()
        val title = intent.getStringExtra("title")
        val event = intent.getStringExtra("event")
        val localitzacio = intent.getStringExtra("localitzacio")

        if(amount >= 0.5) {

            ApiClient().createPaymentIntent(amount!!,title!!,event!!,localitzacio!!,
                completion = { paymentIntentClientSecret, error ->
                    run {
                        paymentIntentClientSecret?.let {
                            Log.d("PAYMENT INTENT CLIENT: ", it.toString())
                            this@OfertFragmentPay.paymentIntentClientSecret = it
                        }
                        error?.let {
                            Log.d("Failed to load PaymentIntent", "Error: $error")
                        }
                    }
                })


            // Hook up the pay button to the card widget and stripe instance
            btnAccept.setOnClickListener { view: View? ->
                loading_payment.visibility = View.VISIBLE

                val cardInputWidget = CardInputWidget(requireContext())
                cardInputWidget.setCardNumber("4242424242424242")
                cardInputWidget.setCvcCode("123")
                cardInputWidget.setExpiryDate(12, 25)
                cardInputWidget.postalCodeEnabled = false

                val params = cardInputWidget.paymentMethodCreateParams

                if (params != null) {
                    val confirmParams =
                        ConfirmPaymentIntentParams.createWithPaymentMethodCreateParams(
                            params,
                            paymentIntentClientSecret!!
                        )
                    Log.d("CONFIRM PARAMS", confirmParams.toString())
                    stripe.confirmPayment(this, confirmParams)
                }
            }
        }else{
            btnAccept.setOnClickListener {
                buttons1st.visibility = View.GONE
                buttons2nd.visibility = View.VISIBLE

                requireFragmentManager().beginTransaction().replace(R.id.fragment_ofertpay, PayCardFragment_Complete()).commit()
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        stripe.onPaymentResult(requestCode, data, object : ApiResultCallback<PaymentIntentResult> {
            override fun onSuccess(result: PaymentIntentResult) {
                val paymentIntent = result.intent
                if (paymentIntent.status == StripeIntent.Status.Succeeded) {
                    val gson = GsonBuilder().setPrettyPrinting().create()
                    Log.d("Payment Succeeded", "${gson.toJson(paymentIntent)}")

                    buttons1st.visibility = View.GONE
                    buttons2nd.visibility = View.VISIBLE

                    loading_payment.visibility = View.GONE

                    requireFragmentManager().beginTransaction().replace(R.id.fragment_ofertpay, PayCardFragment_Complete()).commit()

                } else if (paymentIntent.status == StripeIntent.Status.RequiresPaymentMethod) {
                    Toast.makeText(requireContext(), "ERROR DE PAGAMENT", Toast.LENGTH_SHORT).show()
                    Log.d("Payment failed", "${paymentIntent.lastPaymentError?.message.orEmpty()}")
                }
            }

            override fun onError(e: Exception) {
                Toast.makeText(requireContext(), "ERROR DE PAGAMENT", Toast.LENGTH_SHORT).show()
                Log.d("Payment failed", "${e.toString()}")
            }
        })
    }

}