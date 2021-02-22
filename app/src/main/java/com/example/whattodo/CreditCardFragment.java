package com.example.whattodo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.example.whattodo.adapter.CreditCardAdapter;
import com.example.whattodo.adapter.PayCardOfertAdapter;
import com.example.whattodo.model.CreditCard;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class CreditCardFragment extends Fragment implements View.OnClickListener{

    View mView;
    Button newCard, deleteCards;
    LinearLayout noCreditCards;

    private RecyclerView mRecyclerView;
    private CreditCardAdapter adapter;
    private FirebaseFirestore mFirestore;
    String userID;
    private Query creditCardQuery;
    List<CreditCard> stCards;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_creditcard_menu, container, false);

        mRecyclerView = mView.findViewById(R.id.creditCard_recycler_view);
        newCard = mView.findViewById(R.id.btnNew);
        deleteCards = mView.findViewById(R.id.btnDelete);
        noCreditCards = mView.findViewById(R.id.noCreditCard);

        mFirestore = FirebaseFirestore.getInstance();
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        creditCardQuery = mFirestore.collection("users").document(userID).collection("CreditCards");

        newCard.setOnClickListener(this);
        deleteCards.setOnClickListener(this);

        checkDocuments();
        initRecyclerView(creditCardQuery);

        return mView;
    }

    private void initRecyclerView(Query query) {

        FirestoreRecyclerOptions<CreditCard> options = new FirestoreRecyclerOptions.Builder<CreditCard>().setQuery(query, CreditCard.class).build();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CreditCardAdapter(options);
        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setAdapter(adapter);

    }

    public void updateCreditCards(String documentID, final String numTarjeta){
        mFirestore.collection("users").document(userID).collection("CreditCards").document(documentID)
                .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("DELETE CREDIT CARD", "TARJETA: "+numTarjeta+" ELIMINADA");
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnNew:
                Intent intent = new Intent(getActivity(), CC_ItemCreditCardActivity.class);
                startActivity(intent);
                break;
            case R.id.btnDelete:
                //adapter.getCreditCards();

                for(int i=0; i < adapter.getItemCount(); i++){
                    CreditCard creditCard = adapter.getItem(i);

                    if(creditCard.isSelected() == true){
                        Log.d("NUM-TARGETA-SELECCIONADA", creditCard.getNumTargeta());
                        updateCreditCards(adapter.getSnapshots().getSnapshot(i).getId(), creditCard.getNumTargeta());
                    }
                }
                break;
        }
    }

    private void checkDocuments() {

        creditCardQuery.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        int countNumberCC = 0;
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){
                                Log.d("Copyin documents", document.getId() + " => " + document.getData());
                                countNumberCC++;
                                document.getData().equals("cc_cardVV");
                            }
                            Log.d("How many CreditCards", String.valueOf(countNumberCC));

                            if(countNumberCC > 0 ) {
                                noCreditCards.setVisibility(View.INVISIBLE);
                            }else{
                                noCreditCards.setVisibility(View.VISIBLE);
                            }

                        }
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
        checkDocuments();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
