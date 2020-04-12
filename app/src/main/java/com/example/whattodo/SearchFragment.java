package com.example.whattodo;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SearchFragment extends Fragment{

    LinearLayout btn_home, btn_search, btn_history, btn_profile;

    private RecyclerView mRecyclerView;
    private EntryObjectAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    public ArrayList<EntryObject> searchList;
    public EntryObject_DB database = new EntryObject_DB();
    View mView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_search, container, false);

        database.createDataBase();
        database.addAllToDataBase();
        searchList = database.getDataBase();

        buildRecyclerView();

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


    private void filter(String text){
        ArrayList<EntryObject> filteredList = new ArrayList<>();

        for(EntryObject item : searchList){
            if(item.getTitol().toLowerCase().contains((text.toLowerCase()))){
                filteredList.add(item);
            }
            else if(item.getCiutat().toLowerCase().contains((text.toLowerCase()))){
                filteredList.add(item);
            }
        }
        mAdapter.filterList(filteredList);
    }


    public void buildRecyclerView(){
        mRecyclerView = mView.findViewById(R.id.recycler_view);
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
                bundle.putString("Previous_Layout", "search");
                selectedFragment.setArguments(bundle);

                getFragmentManager().beginTransaction().replace(R.id.fragment_main, selectedFragment).commit();
            }
        });
    }
}
