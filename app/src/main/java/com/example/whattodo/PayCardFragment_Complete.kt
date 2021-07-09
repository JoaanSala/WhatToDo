package com.example.whattodo

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment



class PayCardFragment_Complete : Fragment() {

    private lateinit var pay_amount: TextView
    private lateinit var intent: Intent

    private lateinit var viewOfLayout: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewOfLayout = inflater.inflate(R.layout.fragment_paycard_complete, container, false)

        intent = requireActivity().intent
        pay_amount = viewOfLayout.findViewById(R.id.pay_amount)

        pay_amount.setText(intent.getStringExtra("price") + " €")

        return viewOfLayout
    }
}