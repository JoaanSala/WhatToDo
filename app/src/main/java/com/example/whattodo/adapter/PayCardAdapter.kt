package com.example.whattodo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.whattodo.CCUtils.CreditCardUtils
import com.example.whattodo.R
import com.example.whattodo.adapter.PayCardAdapter.PayCardHolder
import com.example.whattodo.model.CreditCard
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class PayCardAdapter(val creditCards: FirestoreRecyclerOptions<CreditCard>) : FirestoreRecyclerAdapter<CreditCard, PayCardHolder>(creditCards) {

    inner class PayCardHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mImageCard: ImageView
        var mCardNumberText: TextView
        var mCardDateValidate: TextView
        var radioButton: RadioButton

        init {
            mImageCard = itemView.findViewById(R.id.imageOfertCard)
            mCardNumberText = itemView.findViewById(R.id.textCardNumberOfert)
            mCardDateValidate = itemView.findViewById(R.id.textValidateDateOfert)
            radioButton = itemView.findViewById(R.id.radioButton_payied)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PayCardHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_ofert_ccpayment, parent, false)
        return PayCardHolder(v)
    }

    override fun onBindViewHolder(holder: PayCardHolder, i: Int, creditCard: CreditCard) {
        val cardType = CreditCardUtils.getCardType(creditCard.numTargeta!!)
        if (cardType == 1) {
            holder.mImageCard.setImageDrawable(ContextCompat.getDrawable(holder.mImageCard.context, R.drawable.ic_visa))
        } else if (cardType == 2) {
            holder.mImageCard.setImageDrawable(ContextCompat.getDrawable(holder.mImageCard.context, R.drawable.ic_mastercard))
        } else if (cardType == 3) {
            holder.mImageCard.setImageDrawable(ContextCompat.getDrawable(holder.mImageCard.context, R.drawable.ic_discover))
        } else {
            holder.mImageCard.setImageDrawable(ContextCompat.getDrawable(holder.mImageCard.context, R.drawable.ic_amex))
        }
        holder.mCardNumberText.text = "**** **** **** " + creditCard.numTargeta!!.substring(15, 19)
        holder.mCardDateValidate.text = creditCard.expiritDate
    }
}