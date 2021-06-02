package com.example.whattodo.CCUtils

import android.R
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView

/**
 * Created by Jaison on 27/05/17.
 */
class CreditCardFormattingTextWatcher : TextWatcher {
    private var etCard: EditText
    private lateinit var tvCard: TextView
    private lateinit var ivType: ImageView
    private var isDelete = false
    private lateinit var creditCardType: CreditCardType

    constructor(etcard: EditText, tvcard: TextView) {
        this.etCard = etcard
        this.tvCard = tvcard
    }

    constructor(etcard: EditText, tvcard: TextView, creditCardType: CreditCardType) {
        this.etCard = etcard
        this.tvCard = tvcard
        this.creditCardType = creditCardType
    }

    constructor(etcard: EditText, tvcard: TextView, ivType: ImageView, creditCardType: CreditCardType) {
        this.etCard = etcard
        this.tvCard = tvcard
        this.ivType = ivType
        this.creditCardType = creditCardType
    }

    constructor(etcard: EditText) {
        this.etCard = etcard
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        isDelete = if (before == 0) false else true
    }

    override fun afterTextChanged(s: Editable) {
        val source = s.toString()
        val length = source.length
        val stringBuilder = StringBuilder()
        stringBuilder.append(source)

        if (length > 0 && length % 5 == 0) {
            if (isDelete) stringBuilder.deleteCharAt(length - 1) else stringBuilder.insert(length - 1, " ")
            etCard.setText(stringBuilder)
            etCard.setSelection(etCard.text.length)
        }
        if (length >= 4 && creditCardType != null) creditCardType.setCardType(CreditCardUtils.getCardType(source.trim { it <= ' ' }))
        if (tvCard != null) {
            if (length == 0) tvCard!!.text = "XXXX XXXX XXXX XXXX" else tvCard.text = stringBuilder
        }
        if (ivType != null && length == 0) ivType.setImageResource(R.color.transparent)
    }

    interface CreditCardType {
        fun setCardType(type: Int)
    }
}