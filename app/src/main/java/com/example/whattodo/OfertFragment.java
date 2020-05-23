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

public class OfertFragment extends Fragment{

    View mView;
    TextView text_NOLocation;

    private RecyclerView recyclerView_Ofert;
    FirestoreRecyclerOptions<Ofert> mOptions;
    private OfertAdapter mAdapter;
    private FirebaseFirestore mFirestore;
    private CollectionReference ofertRef;
    FirebaseAuth mAuth;
    String userID;
    MainActivity ma;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_ofert, container, false);

        ma = (MainActivity)getActivity();
        boolean locationIsActive = ma.getLocationActivated();

        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        mFirestore = FirebaseFirestore.getInstance();
        ofertRef = mFirestore.collection("users");

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
        Query query = ofertRef.document(userID).collection("userOferts").whereEqualTo("localitzacio", ma.getLocation());

        mOptions = new FirestoreRecyclerOptions.Builder<Ofert>().setQuery(query, Ofert.class).build();

        mAdapter = new OfertAdapter(mOptions);

        recyclerView_Ofert.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView_Ofert.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new OfertAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                String documentId = documentSnapshot.getId();
                updateUserOfert(documentId);
            }
        });

    }

    private Task<Void> updateUserOfert(String documentId) {

        final DocumentReference ofertDocument = ofertRef.document(userID).collection("userOferts").document(documentId);

        return mFirestore.runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void apply(Transaction transaction)
                    throws FirebaseFirestoreException {

                Ofert ofert = transaction.get(ofertDocument)
                        .toObject(Ofert.class);

                boolean adquirit = true;
                ofert.setAdquirit(adquirit);

                transaction.set(ofertDocument, ofert);
                return null;
            }
        });
    }

    private void checkDocuments() {
        ofertRef.document(userID).collection("userOferts").whereEqualTo("localitzacio", ma.getLocation())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            int count = 0;
                            for(QueryDocumentSnapshot document : task.getResult()){
                                Log.d("Copyin documents", document.getId() + " => " + document.getData());
                                count++;
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
