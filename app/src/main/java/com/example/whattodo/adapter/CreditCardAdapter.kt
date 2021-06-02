package com.example.whattodo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.whattodo.CCUtils.CreditCardUtils
import com.example.whattodo.R
import com.example.whattodo.adapter.CreditCardAdapter.CreditCardHolder
import com.example.whattodo.model.CreditCard
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class CreditCardAdapter(val creditCards: FirestoreRecyclerOptions<CreditCard>) : FirestoreRecyclerAdapter<CreditCard, CreditCardHolder>(creditCards) {

    inner class CreditCardHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mImageCard: ImageView
        var mCardNumberText: TextView
        var mCardDateValidate: TextView
        var checkBox: CheckBox

        init {
            mImageCard = itemView.findViewById(R.id.imageCard)
            mCardNumberText = itemView.findViewById(R.id.textCardNumber)
            mCardDateValidate = itemView.findViewById(R.id.textValidateDate)
            checkBox = itemView.findViewById(R.id.checkbox)
            checkBox.setOnClickListener { creditCards.snapshots[adapterPosition].isSelected = checkBox.isChecked }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreditCardHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_creditcard, parent, false)
        return CreditCardHolder(v)
    }

    override fun onBindViewHolder(holder: CreditCardHolder, position: Int, creditCard: CreditCard) {
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
        holder.mCardDateValidate.text = "Expira: " + creditCard.expiritDate
        holder.checkBox.isChecked = false
    }
}