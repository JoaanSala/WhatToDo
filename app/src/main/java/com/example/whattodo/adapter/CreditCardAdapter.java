package com.example.whattodo.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
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



public class CreditCardAdapter extends FirestoreRecyclerAdapter<CreditCard, CreditCardAdapter.CreditCardHolder> {
    private FirestoreRecyclerOptions<CreditCard> ccList;

    public CreditCardAdapter(@NonNull FirestoreRecyclerOptions<CreditCard> options) {
        super(options);
        this.ccList = options;
    }

    public class CreditCardHolder extends RecyclerView.ViewHolder {
        public ImageView mImageCard;
        public TextView mCardNumberText, mCardDateValidate;
        public CheckBox checkBox;

        public CreditCardHolder (@NonNull View itemView){
            super(itemView);
            mImageCard = itemView.findViewById(R.id.imageCard);
            mCardNumberText = itemView.findViewById(R.id.textCardNumber);
            mCardDateValidate = itemView.findViewById(R.id.textValidateDate);
            checkBox = itemView.findViewById(R.id.checkbox);

            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ccList.getSnapshots().get(getAdapterPosition()).setSelected(checkBox.isChecked());
                }
            });
        }
    }


    @Override
    public CreditCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_creditcard, parent, false);
        return new CreditCardHolder(v);
    }


    @Override
    protected void onBindViewHolder(@NonNull CreditCardHolder holder, int position, @NonNull final CreditCard creditCard) {

        CreditCardUtils ccUtils = new CreditCardUtils();
        int cardType = ccUtils.getCardType(creditCard.getNumTargeta());

        if(cardType == 1){
            holder.mImageCard.setImageDrawable(ContextCompat.getDrawable(holder.mImageCard.getContext(), R.drawable.ic_visa));
        }else if(cardType == 2){
            holder.mImageCard.setImageDrawable(ContextCompat.getDrawable(holder.mImageCard.getContext(), R.drawable.ic_mastercard));
        }else if(cardType == 3){
            holder.mImageCard.setImageDrawable(ContextCompat.getDrawable(holder.mImageCard.getContext(), R.drawable.ic_discover));
        }else{
            holder.mImageCard.setImageDrawable(ContextCompat.getDrawable(holder.mImageCard.getContext(), R.drawable.ic_amex));
        }

        holder.mCardNumberText.setText("**** **** **** " +creditCard.getNumTargeta().substring(15,19));
        holder.mCardDateValidate.setText("Expira: "+creditCard.getExpiritDate());

        holder.checkBox.setChecked(false);
    }

    public FirestoreRecyclerOptions<CreditCard> getCreditCards(){
        return ccList;
    }


}
