package com.example.whattodo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.whattodo.adapter.PayCardAdapter;
import com.example.whattodo.model.CreditCard;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class PayCardFragment extends Fragment{

    View mView;
    private RecyclerView mRecyclerView;
    LinearLayout noCreditCards;
    Button btnAccept, btnCancell;

    private PayCardAdapter adapter;
    private FirebaseFirestore mFirestore;
    String userID;
    private Query creditCardQuery;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_paycard, container, false);

        Log.d("FRAGMENT-PAYMENT", "OK");
        mRecyclerView = mView.findViewById(R.id.dialog_rv);
        noCreditCards = mView.findViewById(R.id.noCreditCard_payied);

        mFirestore = FirebaseFirestore.getInstance();
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        creditCardQuery = mFirestore.collection("users").document(userID).collection("CreditCards");

        checkDocuments();
        initReciclerView(creditCardQuery);

        return mView;
    }

    private void initReciclerView(Query query) {
        FirestoreRecyclerOptions<CreditCard> options = new FirestoreRecyclerOptions.Builder<CreditCard>().setQuery(query, CreditCard.class).build();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new PayCardAdapter(options);
        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setAdapter(adapter);
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
