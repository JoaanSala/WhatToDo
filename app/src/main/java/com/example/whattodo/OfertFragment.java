package com.example.whattodo;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.whattodo.adapter.EventAdapter;
import com.example.whattodo.adapter.OfertAdapter;
import com.example.whattodo.model.Event;
import com.example.whattodo.model.Ofert;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.google.firestore.v1.WriteResult;

import java.util.HashMap;
import java.util.Map;

public class OfertFragment extends Fragment{

    private static final String TAG = "OfertFragment";

    View mView;
    TextView text_NOLocation;
    String dialogAnswer;

    private RecyclerView recyclerView_Ofert;
    FirestoreRecyclerOptions<Ofert> mOptions;
    private OfertAdapter mAdapter;
    private FirebaseFirestore mFirestore;
    private CollectionReference ofertRef;
    FirebaseAuth mAuth;
    String userID;
    MainActivity ma;
    int count = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_ofert, container, false);

        ma = (MainActivity)getActivity();
        boolean locationIsActive = ma.getLocationActivated();

        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        mFirestore = FirebaseFirestore.getInstance();
        ofertRef = mFirestore.collection("oferts");
        dialogAnswer = "";

        recyclerView_Ofert = mView.findViewById(R.id.recyclerview_oferts);
        recyclerView_Ofert.setHasFixedSize(true);
        text_NOLocation = mView.findViewById(R.id.text_NOLocation);

        if(locationIsActive){
            recyclerView_Ofert.setVisibility(View.VISIBLE);
            text_NOLocation.setVisibility(View.GONE);
            initRecyclerView();
        }

        return mView;
    }

    private void initRecyclerView() {
        checkDocuments();
        Query query = ofertRef.whereEqualTo("localitzacio", ma.getLocation());

        mOptions = new FirestoreRecyclerOptions.Builder<Ofert>().setQuery(query, Ofert.class).build();
        Log.d("OPTIONS", query.toString());
        mAdapter = new OfertAdapter(mOptions);

        recyclerView_Ofert.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView_Ofert.setAdapter(mAdapter);

        //if(userEdat > 18) {
            mAdapter.setOnItemClickListener(new OfertAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(DocumentSnapshot documentSnapshot, int position) {

                    PayCardDialogFragment dialogFragment = new PayCardDialogFragment();
                    dialogFragment.show(getFragmentManager(), "ofertPayment");

                    if(dialogAnswer.equals("OK")) {
                        /*String documentId = documentSnapshot.getId();
                        Log.d("ofertaID", documentId);
                        updateUserOfert(documentId, documentSnapshot);*/
                        Toast.makeText(getContext(), "OK", Toast.LENGTH_SHORT).show();
                    }else{
                        //Toast.makeText(getContext(), dialogAnswer, Toast.LENGTH_SHORT).show();
                    }
                }
            });

        //}
    }

    private void updateUserOfert(final String documentId, DocumentSnapshot documentSnapshot) {

        final DocumentReference ofertDocument = mFirestore.collection("users").document(userID).collection("PaidOferts").document(documentId);

        String TitleOfert = documentSnapshot.get("title").toString();
        String EventOfert = documentSnapshot.get("event").toString();
        String LocalizationOfert = documentSnapshot.get("localitzacio").toString();

        Map<String, Object> data = new HashMap<>();

        data.put("info", TitleOfert+", "+EventOfert+", "+LocalizationOfert);
        data.put("acquired", true);

        ofertDocument.set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("TAG", "onSuccess : ofert Added with Id "+documentId);
            }
        });
    }

    private void checkDocuments() {
        ofertRef.whereEqualTo("localitzacio", ma.getLocation())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        int count = 0;
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){
                                Log.d("Copyin documents", document.getId() + " => " + document.getData());
                                count++;
                            }
                            if(count==0) {
                                text_NOLocation.setVisibility(View.VISIBLE);
                                text_NOLocation.setText("HO SENTIM, PERO EN AQUESTA LOCALITZACIÓ NO TENIM OFERTES EN AQUESTS MOMENTS...");
                            }
                            Log.d("How many Oferts", String.valueOf(count));
                        }
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        if(ma.getLocationActivated()) {
            mAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(ma.getLocationActivated()) {
            mAdapter.stopListening();
        }
    }

}
