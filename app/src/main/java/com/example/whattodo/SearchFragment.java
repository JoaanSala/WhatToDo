package com.example.whattodo;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whattodo.adapter.EventAdapter;
import com.example.whattodo.model.Event;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

public class SearchFragment extends Fragment{

    private RecyclerView mRecyclerView;
    View mView;

    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
    private CollectionReference eventRef = mFirestore.collection("entryObject_DB");

    private EventAdapter mAdapter;
    FirestoreRecyclerOptions<Event> mOptions;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_search, container, false);
        mRecyclerView = mView.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        initRecyclerView();

        EditText editText = mView.findViewById(R.id.editText);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });

        return mView;
    }

    private void initRecyclerView() {
        Log.d("initRecyclerView", "I'm here");
        mOptions = new FirestoreRecyclerOptions.Builder<Event>().setQuery(eventRef, Event.class).build();

        mAdapter = new EventAdapter(mOptions);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new EventAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                String documentId = documentSnapshot.getId();
                Intent intent = new Intent(getContext(), ItemEvent_Activity.class);
                intent.putExtra("DOCUMENT_KEY", documentId);
                startActivity(intent);
            }
        });
    }

    private void filter(String text){
        mAdapter.stopListening();
        Query query = eventRef.orderBy("Titol").startAt(text).endAt(text+"\uf8ff");
        mOptions = new FirestoreRecyclerOptions.Builder<Event>().setQuery(query, Event.class).build();
        mAdapter = new EventAdapter(mOptions);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter.setOnItemClickListener(new EventAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                String documentId = documentSnapshot.getId();
                Intent intent = new Intent(getContext(), ItemEvent_Activity.class);
                intent.putExtra("DOCUMENT_KEY", documentId);
                startActivity(intent);
            }
        });
        mAdapter.startListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        mAdapter.startListening();
        Log.d("onStart", "I'm here!");
    }

    @Override
    public void onStop() {
        super.onStop();
        mAdapter.stopListening();
        Log.d("onStop", "I'm here!");
    }
}
