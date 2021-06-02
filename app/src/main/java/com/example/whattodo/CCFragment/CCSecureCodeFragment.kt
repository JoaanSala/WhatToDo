package com.example.whattodo.CCFragment

import android.content.ContentValues
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import androidx.fragment.app.Fragment
import butterknife.BindView
import butterknife.ButterKnife
import com.example.whattodo.CCUtils.CreditCardEditText
import com.example.whattodo.CCUtils.CreditCardEditText.BackButtonListener
import com.example.whattodo.CC_ItemCreditCardActivity
import com.example.whattodo.R

/**
 * A simple [Fragment] subclass.
 */
class CCSecureCodeFragment : Fragment() {
    @BindView(R.id.et_cvv)
    private lateinit var et_cvv: CreditCardEditText
    private lateinit var tv_cvv: TextView
    private lateinit var cc_activity: CC_ItemCreditCardActivity

    private lateinit var viewOfLayout: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        viewOfLayout = inflater.inflate(R.layout.fragment_ccsecure_code, container, false)
        ButterKnife.bind(this, viewOfLayout)

        cc_activity = activity as CC_ItemCreditCardActivity
        et_cvv = viewOfLayout.findViewById(R.id.et_cvv)

        et_cvv.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                if (tv_cvv != null) {
                    if (TextUtils.isEmpty(editable.toString().trim { it <= ' ' })) tv_cvv.text = "XXX" else tv_cvv.text = editable.toString()
                } else Log.d(ContentValues.TAG, "afterTextChanged: cvv null")
            }
        })
        et_cvv.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (cc_activity != null) {
                    cc_activity.nextClick()
                    return@OnEditorActionListener true
                }
            }
            false
        })
        et_cvv.setOnBackButtonListener(object : BackButtonListener {
            override fun onBackClick() {
                if (cc_activity != null) cc_activity.onBackPressed()
            }
        })

        return viewOfLayout
    }

    fun setCvv(tv: TextView) {
        tv_cvv = tv
    }

    val value: String
        get() {
            var getValue = ""
            if (et_cvv != null) {
                getValue = et_cvv.text.toString().trim { it <= ' ' }
            }
            return getValue
        }
}