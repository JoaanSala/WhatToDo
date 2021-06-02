package com.example.whattodo.CCFragment

import android.os.Bundle
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
import com.example.whattodo.CCUtils.CreditCardExpiryTextWatcher
import com.example.whattodo.CC_CardFrontFragment
import com.example.whattodo.CC_ItemCreditCardActivity
import com.example.whattodo.R

/**
 * A simple [Fragment] subclass.
 */
class CCValidityFragment : Fragment() {
    @BindView(R.id.et_validity)
    private lateinit var et_validity: CreditCardEditText
    private lateinit var tv_validity: TextView
    private lateinit var cc_activity: CC_ItemCreditCardActivity
    private lateinit var cardFrontFragment: CC_CardFrontFragment

    private lateinit var viewOfLayout: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        viewOfLayout = inflater.inflate(R.layout.fragment_ccvalidity, container, false)
        ButterKnife.bind(this, viewOfLayout)

        cc_activity = activity as CC_ItemCreditCardActivity
        cardFrontFragment = cc_activity.cardFrontFragment
        et_validity = viewOfLayout.findViewById(R.id.et_validity)
        tv_validity = cardFrontFragment!!.getValidity()

        et_validity.addTextChangedListener(CreditCardExpiryTextWatcher(et_validity, tv_validity))
        et_validity.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (cc_activity != null) {
                    cc_activity.nextClick()
                    return@OnEditorActionListener true
                }
            }
            false
        })
        et_validity.setOnBackButtonListener(object : BackButtonListener {
            override fun onBackClick() {
                if (cc_activity != null) cc_activity.onBackPressed()
            }
        })

        return viewOfLayout
    }

    val validity: String?
        get() = if (et_validity != null) et_validity.text.toString().trim { it <= ' ' } else null
}