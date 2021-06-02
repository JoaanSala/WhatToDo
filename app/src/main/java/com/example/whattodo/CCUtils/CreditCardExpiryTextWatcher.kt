package com.example.whattodo.CCUtils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView

/**
 * Created by Jaison on 28/05/17.
 */
class CreditCardExpiryTextWatcher : TextWatcher {
    private lateinit var etCard: EditText
    private lateinit var tvCard: TextView
    private var isDelete = false

    constructor(etcard: EditText, tvcard: TextView) {
        etCard = etcard
        tvCard = tvcard
    }

    constructor(etcard: EditText) {
        etCard = etcard
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
        if (length > 0 && length == 3) {
            if (isDelete) stringBuilder.deleteCharAt(length - 1) else stringBuilder.insert(length - 1, "/")
            etCard.setText(stringBuilder)
            etCard.setSelection(etCard.text.length)

            // Log.d("test"+s.toString(), "afterTextChanged: append "+length);
        }
        if (tvCard != null) {
            if (length == 0) tvCard!!.text = "MM/YY" else tvCard!!.text = stringBuilder
        }
    }
}