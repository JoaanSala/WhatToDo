package com.example.whattodo.CCFragment

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
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
import com.example.whattodo.CC_CardFrontFragment
import com.example.whattodo.CC_ItemCreditCardActivity
import com.example.whattodo.R

/**
 * A simple [Fragment] subclass.
 */
class CCNameFragment : Fragment() {
    @BindView(R.id.et_name)
    private lateinit var et_name: CreditCardEditText

    private lateinit var tv_Name: TextView

    private lateinit var cc_activity: CC_ItemCreditCardActivity
    private lateinit var cardFrontFragment: CC_CardFrontFragment

    private lateinit var viewOfLayout: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        viewOfLayout = inflater.inflate(R.layout.fragment_ccname, container, false)
        ButterKnife.bind(this, viewOfLayout)

        cc_activity = activity as CC_ItemCreditCardActivity
        cardFrontFragment = cc_activity.cardFrontFragment

        tv_Name = cardFrontFragment.getName()

        et_name = viewOfLayout.findViewById(R.id.et_name)
        et_name.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                if (tv_Name != null) {
                    if (TextUtils.isEmpty(editable.toString().trim { it <= ' ' })) tv_Name.text = "CARD HOLDER" else tv_Name.text = editable.toString()
                }
            }
        })
        et_name.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (cc_activity != null) {
                    cc_activity.nextClick()
                    return@OnEditorActionListener true
                }
            }
            false
        })
        et_name.setOnBackButtonListener(object : BackButtonListener {
            override fun onBackClick() {
                if (cc_activity != null) cc_activity.onBackPressed()
            }
        })
        return viewOfLayout
    }

    val name: String?
        get() = if (et_name != null) et_name.text.toString().trim { it <= ' ' } else null
}