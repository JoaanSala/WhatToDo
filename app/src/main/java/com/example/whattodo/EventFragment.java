package com.example.whattodo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class EventFragment extends Fragment{

    public EntryObject_DB database = new EntryObject_DB();
    private RecyclerView mRecyclerView;
    private EntryObjectAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    public ArrayList<EntryObject> searchList;
    View mView;
    String message;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_event, container, false);

        Bundle bundle = getArguments();

        message = bundle.getString("Type_Event");

        TextView titol = mView.findViewById(R.id.textEvent);
        titol.setText(message);
        database.createDataBase();

        if(message.equals("Restaurants")){
            database.addRestaurantsToDataBase();
        }
        else if(message.equals("Llocs d'Interès")){
            database.addMonumentsToDataBase();
        }
        else if(message.equals("Oci")){
            database.addOciToDataBase();
        }
        else if(message.equals("Nocturn")){
            database.addNightToDataBase();
        }

        searchList = database.getDataBase();
        buildRecyclerView();

        ImageView back = mView.findViewById(R.id.arrow_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_main, new HomeFragment()).commit();
            }
        });

        return mView;
    }


    public void buildRecyclerView(){
        mRecyclerView = mView.findViewById(R.id.recycler_view_events);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mAdapter = new EntryObjectAdapter((searchList));

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new EntryObjectAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Fragment selectedFragment = new ItemEvent_Fragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable("Entry_Object", searchList.get(position));
                bundle.putString("Previous_Layout", message);
                selectedFragment.setArguments(bundle);

                getFragmentManager().beginTransaction().replace(R.id.fragment_main, selectedFragment).commit();
            }
        });
    }
}
