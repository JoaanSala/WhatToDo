package com.example.whattodo;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.whattodo.CCUtils.FontTypeChange;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.whattodo.CCUtils.CreditCardUtils.AMEX;
import static com.example.whattodo.CCUtils.CreditCardUtils.DISCOVER;
import static com.example.whattodo.CCUtils.CreditCardUtils.MASTERCARD;
import static com.example.whattodo.CCUtils.CreditCardUtils.NONE;
import static com.example.whattodo.CCUtils.CreditCardUtils.VISA;


/**
 * A simple {@link Fragment} subclass.
 */
public class CC_CardFrontFragment extends Fragment {


    @BindView(R.id.tv_card_number)
    TextView tvNumber;
    @BindView(R.id.tv_member_name)
    TextView tvName;
    @BindView(R.id.tv_validity)
    TextView tvValidity;
    @BindView(R.id.ivType)
    ImageView ivType;

    FontTypeChange fontTypeChange;

    public CC_CardFrontFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view=inflater.inflate(R.layout.fragment_card_front, container, false);
        ButterKnife.bind(this, view);

        tvNumber = view.findViewById(R.id.tv_card_number);
        tvName = view.findViewById(R.id.tv_member_name);
        ivType = view.findViewById(R.id.ivType);

        fontTypeChange=new FontTypeChange(getActivity());
        tvNumber.setTypeface(fontTypeChange.get_fontface(3));
        tvName.setTypeface(fontTypeChange.get_fontface(3));

        return view;
    }

    public TextView getNumber()
    {
        return tvNumber;
    }
    public TextView getName()
    {
        return tvName;
    }
    public TextView getValidity()
    {
        return tvValidity;
    }

    public ImageView getCardType()
    {
        return ivType;
    }


    public void setCardType(int type)
    {
        switch(type)
        {
            case VISA:
                ivType.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_visa));
                break;
            case MASTERCARD:
                ivType.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_mastercard));
                break;
            case AMEX:
                ivType.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_amex));
                break;
            case DISCOVER:
                ivType.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_discover));
                break;
            case NONE:
                ivType.setImageResource(android.R.color.transparent);
            break;

        }


    }


}
