package com.example.whattodo

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import butterknife.BindView
import butterknife.ButterKnife
import com.example.whattodo.CCFragment.CCSecureCodeFragment
import com.example.whattodo.CCUtils.FontTypeChange

/**
 * A simple [Fragment] subclass.
 */
class CC_CardBackFragment : Fragment() {
    @BindView(R.id.tv_cvv)
    private lateinit var tv_cvv: TextView
    private lateinit var fontTypeChange: FontTypeChange
    private lateinit var cc_activity: CC_ItemCreditCardActivity
    private lateinit var securecode: CCSecureCodeFragment

    private lateinit var viewOfLayout: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        viewOfLayout = inflater.inflate(R.layout.fragment_card_back, container, false)
        ButterKnife.bind(this, viewOfLayout)

        tv_cvv = viewOfLayout.findViewById(R.id.tv_cvv)
        fontTypeChange = FontTypeChange(activity)
        tv_cvv.setTypeface(fontTypeChange!!.get_fontface(1))

        cc_activity = activity as CC_ItemCreditCardActivity
        securecode = cc_activity.secureCodeFragment
        securecode.setCvv(tv_cvv)

        if (!TextUtils.isEmpty(securecode.value)) tv_cvv.setText(securecode.value)

        return viewOfLayout
    }
}