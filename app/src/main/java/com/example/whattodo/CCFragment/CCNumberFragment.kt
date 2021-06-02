package com.example.whattodo.CCFragment

import android.os.Bundle
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
import com.example.whattodo.CCUtils.CreditCardFormattingTextWatcher
import com.example.whattodo.CC_CardFrontFragment
import com.example.whattodo.CC_ItemCreditCardActivity
import com.example.whattodo.R

/**
 * A simple [Fragment] subclass.
 */
class CCNumberFragment : Fragment() {
    @BindView(R.id.et_number)
    private lateinit var et_number: CreditCardEditText
    private lateinit var tv_number: TextView
    private lateinit var cc_activity: CC_ItemCreditCardActivity
    lateinit var cardFrontFragment: CC_CardFrontFragment

    private lateinit var viewOfLayout: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        viewOfLayout = inflater.inflate(R.layout.fragment_ccnumber, container, false)
        ButterKnife.bind(this, viewOfLayout)

        cc_activity = activity as CC_ItemCreditCardActivity
        cardFrontFragment = cc_activity.cardFrontFragment
        et_number = viewOfLayout.findViewById(R.id.et_number)
        tv_number = cardFrontFragment.getNumber()

        //Do your stuff
        et_number.addTextChangedListener(CreditCardFormattingTextWatcher(et_number, tv_number, cardFrontFragment.getCardType(), object : CreditCardFormattingTextWatcher.CreditCardType {
            override fun setCardType(type: Int) {
                Log.d("Card", "setCardType: $type")
                cardFrontFragment.setCardType(type)
            }
        }))
        et_number.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (cc_activity != null) {
                    cc_activity.nextClick()
                    return@OnEditorActionListener true
                }
            }
            false
        })
        et_number.setOnBackButtonListener(object : BackButtonListener {
            override fun onBackClick() {
                if (activity != null) activity!!.onBackPressed()
            }
        })
        return viewOfLayout
    }

    val cardNumber: String?
        get() = if (et_number != null) et_number.text.toString().trim { it <= ' ' } else null
}