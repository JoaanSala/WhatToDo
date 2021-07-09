package com.example.whattodo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import butterknife.BindView
import butterknife.ButterKnife
import com.example.whattodo.CCUtils.CreditCardUtils
import com.example.whattodo.CCUtils.FontTypeChange
import kotlinx.android.synthetic.main.fragment_card_front.*

/**
 * A simple [Fragment] subclass.
 */
class CC_CardFrontFragment : Fragment() {
    @BindView(R.id.tv_card_number)
    private lateinit var number: TextView

    @BindView(R.id.tv_member_name)
    private lateinit var name: TextView

    @BindView(R.id.tv_validity)
    private lateinit var validity: TextView

    @BindView(R.id.ivType)
    private lateinit var cardType: ImageView
    private lateinit var fontTypeChange: FontTypeChange

    private lateinit var viewOfLayout: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        viewOfLayout = inflater.inflate(R.layout.fragment_card_front, container, false)
        ButterKnife.bind(this, viewOfLayout)

        number = viewOfLayout.findViewById(R.id.tv_card_number)
        name = viewOfLayout.findViewById(R.id.tv_member_name)
        cardType = viewOfLayout.findViewById(R.id.ivType)
        validity = viewOfLayout.findViewById(R.id.tv_validity)

        fontTypeChange = FontTypeChange(activity)

        number.setTypeface(fontTypeChange!!.get_fontface(3))
        name.setTypeface(fontTypeChange!!.get_fontface(3))

        return viewOfLayout
    }

    fun getName(): TextView { return name }
    fun getNumber(): TextView { return number }
    fun getValidity(): TextView { return validity }
    fun getCardType(): ImageView { return cardType }

    fun setCardType(type: Int) {
        when (type) {
            CreditCardUtils.VISA -> cardType.setImageDrawable(ContextCompat.getDrawable(requireActivity(), R.drawable.ic_visa))
            CreditCardUtils.MASTERCARD -> cardType.setImageDrawable(ContextCompat.getDrawable(requireActivity(), R.drawable.ic_mastercard))
            CreditCardUtils.AMEX -> cardType.setImageDrawable(ContextCompat.getDrawable(requireActivity(), R.drawable.ic_amex))
            CreditCardUtils.DISCOVER -> cardType.setImageDrawable(ContextCompat.getDrawable(requireActivity(), R.drawable.ic_discover))
            CreditCardUtils.NONE -> cardType.setImageResource(android.R.color.transparent)
        }
    }
}