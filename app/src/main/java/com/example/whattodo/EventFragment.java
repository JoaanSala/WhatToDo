package com.example.whattodo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.whattodo.adapter.EventAdapter;
import com.example.whattodo.model.Event;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class EventFragment extends Fragment{

    private RecyclerView mRecyclerView;
    View mView;
    String message;

    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
    private CollectionReference eventRef = mFirestore.collection("entryObject_DB");

    private EventAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_event, container, false);

        Bundle bundle = getArguments();
        message = bundle.getString("Type_Event");

        TextView titol = mView.findViewById(R.id.textEvent);
        titol.setText(message);

        initRecyclerView();

        ImageView back = mView.findViewById(R.id.arrow_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_main, new HomeFragment()).commit();
            }
        });

        return mView;
    }

    private void initRecyclerView() {
        Query query = eventRef.whereEqualTo("TypeID", message);

        FirestoreRecyclerOptions<Event> options = new FirestoreRecyclerOptions.Builder<Event>().setQuery(query, Event.class).build();

        adapter = new EventAdapter(options);

        mRecyclerView = mView.findViewById(R.id.recycler_view_events);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new EventAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                String documentId = documentSnapshot.getId();
                Intent intent = new Intent(getContext(), ItemEvent_Activity.class);
                intent.putExtra("DOCUMENT_KEY", documentId);
                startActivity(intent);
            }
        });
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
