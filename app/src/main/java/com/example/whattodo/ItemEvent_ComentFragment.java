package com.example.whattodo;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.whattodo.adapter.ComentAdapter;
import com.example.whattodo.adapter.EventAdapter;
import com.example.whattodo.model.Coment;
import com.example.whattodo.model.Event;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;


public class ItemEvent_ComentFragment extends Fragment {

    View mView;
    private RecyclerView mRecyclerView;
    private ComentAdapter adapter;
    private FirebaseFirestore mFirestore;
    DocumentReference eventRef;
    String documentID;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_eventitem_coment, container, false);

        documentID = getActivity().getIntent().getExtras().getString("DOCUMENT_KEY");
        mFirestore = FirebaseFirestore.getInstance();
        eventRef = mFirestore.collection("entryObject_DB").document(documentID);

        Query query = eventRef
                .collection("coment")
                .limit(10);

        initRecyclerView(query);

        return mView;
    }

    private void initRecyclerView(Query query) {

        FirestoreRecyclerOptions<Coment> options = new FirestoreRecyclerOptions.Builder<Coment>().setQuery(query, Coment.class).build();

        adapter = new ComentAdapter(options);

        mRecyclerView = mView.findViewById(R.id.coment_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}

