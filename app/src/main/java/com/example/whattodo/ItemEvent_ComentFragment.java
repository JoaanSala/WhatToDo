package com.example.whattodo;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


public class ItemEvent_ComentFragment extends Fragment {

    View mView;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    public ArrayList<EntryComent> searchList;
    public EntryComent_DB database = new EntryComent_DB();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_eventitem_coment, container, false);

        Log.i("ComentFragment", "Estic a ComentFragment");

        database.createDB();
        database.addComentExamples();
        searchList = database.getEntryComentDB();
        buildRecyclerView();

        return mView;
    }

    public void buildRecyclerView(){
        mRecyclerView = mView.findViewById(R.id.coment_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new EntryComentAdapter(searchList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

    }
}

