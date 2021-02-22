package com.example.whattodo.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whattodo.CCUtils.CreditCardUtils;
import com.example.whattodo.R;
import com.example.whattodo.model.CreditCard;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;


public class PayCardOfertAdapter extends FirestoreRecyclerAdapter<CreditCard, PayCardOfertAdapter.PayCardOfertHolder> {

    public PayCardOfertAdapter(@NonNull FirestoreRecyclerOptions<CreditCard> options) {
        super(options);
    }

    public class PayCardOfertHolder extends RecyclerView.ViewHolder {
        public ImageView mImageCard;
        public TextView mCardNumberText, mCardDateValidate;
        public RadioButton radioButton;

        public PayCardOfertHolder (@NonNull View itemView){
            super(itemView);
            mImageCard = itemView.findViewById(R.id.imageOfertCard);
            mCardNumberText = itemView.findViewById(R.id.textCardNumberOfert);
            mCardDateValidate = itemView.findViewById(R.id.textValidateDateOfert);
            radioButton = itemView.findViewById(R.id.radioButton);

        }
    }

    @Override
    public PayCardOfertHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("ESTIC AQUI", "OK");
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ofert_ccpayment, parent, false);
        return new PayCardOfertAdapter.PayCardOfertHolder(v);
    }


    @Override
    protected void onBindViewHolder(@NonNull PayCardOfertAdapter.PayCardOfertHolder holder, int position, @NonNull final CreditCard creditCard) {

        CreditCardUtils ccUtils = new CreditCardUtils();
        int cardType = ccUtils.getCardType(creditCard.getNumTargeta());

        if (cardType == 1) {
            holder.mImageCard.setImageDrawable(ContextCompat.getDrawable(holder.mImageCard.getContext(), R.drawable.ic_visa));
        } else if (cardType == 2) {
            holder.mImageCard.setImageDrawable(ContextCompat.getDrawable(holder.mImageCard.getContext(), R.drawable.ic_mastercard));
        } else if (cardType == 3) {
            holder.mImageCard.setImageDrawable(ContextCompat.getDrawable(holder.mImageCard.getContext(), R.drawable.ic_discover));
        } else {
            holder.mImageCard.setImageDrawable(ContextCompat.getDrawable(holder.mImageCard.getContext(), R.drawable.ic_amex));
        }

        holder.mCardNumberText.setText("**** **** **** " + creditCard.getNumTargeta().substring(15, 19));
        holder.mCardDateValidate.setText(creditCard.getExpiritDate());
    }
}
