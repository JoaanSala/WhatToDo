package com.example.whattodo;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.whattodo.adapter.PayCardOfertAdapter;
import com.example.whattodo.model.CreditCard;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class PayCardDialogFragment extends DialogFragment implements View.OnClickListener{

    private static final String TAG = "ofertPayment";

    View mView;
    Button acceptPayment, cancellPayment;
    TextView titleOfert, titleLocation, payAmount;

    private RecyclerView mRecyclerView;
    private PayCardOfertAdapter adapter;
    private FirebaseFirestore mFirestore;
    String userID;
    private Query creditCardQuery;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_paycard, container, false);

        mRecyclerView = mView.findViewById(R.id.rv_dialog);

        acceptPayment = mView.findViewById(R.id.btnAccept);
        cancellPayment = mView.findViewById(R.id.btnCancell);
        titleOfert = mView.findViewById(R.id.ofert_title);
        titleLocation = mView.findViewById(R.id.ofert_location);
        payAmount = mView.findViewById(R.id.pay_amount);

        acceptPayment.setOnClickListener(this);
        cancellPayment.setOnClickListener(this);

        mFirestore = FirebaseFirestore.getInstance();
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        creditCardQuery = mFirestore.collection("users").document(userID).collection("CreditCards");

        initRecyclerView(creditCardQuery);

        return mView;
    }

    public void initRecyclerView(Query query){
        FirestoreRecyclerOptions<CreditCard> options = new FirestoreRecyclerOptions.Builder<CreditCard>().setQuery(query, CreditCard.class).build();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        Log.d("ADAPTER-AVANS", String.valueOf(adapter));
        adapter = new PayCardOfertAdapter(options);
        Log.d("ADAPTER-AVANS", String.valueOf(adapter));
        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnCancell:
                dismiss();
                break;
            case R.id.btnAccept:
                /*String input = "OK";
                OfertFragment ofertFragment = (OfertFragment) getFragmentManager().findFragmentByTag("OfertFragment");
                ofertFragment.dialogAnswer = input;*/

                dismiss();
                break;
        }
    }


}
