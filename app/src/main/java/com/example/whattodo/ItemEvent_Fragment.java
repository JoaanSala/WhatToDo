package com.example.whattodo;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ItemEvent_Fragment extends Fragment implements View.OnClickListener{

    RelativeLayout background_restaurant;
    TextView b_close, tv_title_event, tv_localitzacio;
    String titol, localitzacio;
    Button b_info, b_comentari;
    FragmentManager manager;
    FragmentTransaction transaction;

    View mView;
    Bundle bundle;
    EntryObject entryObject;
    String message;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_eventitem, container, false);

        int imatgeRestaurant = entryObject.getmImageResource();
        titol = entryObject.getTitol();
        localitzacio = entryObject.getLocalitzacio();


        background_restaurant = mView.findViewById(R.id.restaurant_background);
        tv_title_event = mView.findViewById(R.id.title_event);
        tv_localitzacio = mView.findViewById(R.id.localitzacio);

        background_restaurant.setBackgroundResource(imatgeRestaurant);
        tv_title_event.setText(titol);
        tv_localitzacio.setText(localitzacio);


        b_close = mView.findViewById(R.id.close_activity);
        b_close.setOnClickListener(this);


        b_info = mView.findViewById(R.id.b_info);
        b_comentari = mView.findViewById(R.id.b_comentari);
        b_info.setOnClickListener(this);
        b_comentari.setOnClickListener(this);

        return mView;
    }

    public EntryObject getEntryObject(){

        bundle = getArguments();
        entryObject = bundle.getParcelable("Entry_Object");
        message = bundle.getString("Previous_Layout");
        return entryObject;
    }

    public void changetoComent(){
        Bundle bundlecoment = new Bundle();
        bundlecoment.putParcelable("ItemEvent_EntryObjectPrevious", entryObject);
        bundlecoment.putString("ItemEvent_LayoutPrevious", message);
        ComentFragment fragment = new ComentFragment();
        fragment.setArguments(bundlecoment);
        getFragmentManager().beginTransaction().replace(R.id.fragment_main, fragment).commit();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.close_activity:
                if(message.equals("search")){
                    getFragmentManager().beginTransaction().replace(R.id.fragment_main, new SearchFragment()).commit();
                }else{
                    Fragment selectedFragment = null;
                    Bundle newBundle = new Bundle();
                    newBundle.putString("Type_Event", message);
                    selectedFragment = new EventFragment();
                    selectedFragment.setArguments(newBundle);
                    getFragmentManager().beginTransaction().replace(R.id.fragment_main, selectedFragment).commit();
                }
                break;

            case R.id.b_info:
                b_comentari.setBackgroundResource(R.drawable.btn_fragment_u);
                b_comentari.setTextColor(Color.BLACK);
                b_info.setBackgroundResource(R.drawable.btn_fragment_c);
                b_info.setTextColor(Color.WHITE);

                ItemEvent_InfoFragment itemEventInfoFragment = new ItemEvent_InfoFragment();
                manager = getFragmentManager();
                bundle = new Bundle();
                bundle.putParcelable("Item_Object", entryObject);
                bundle.putString("Previous_Layout", message);
                itemEventInfoFragment.setArguments(bundle);
                transaction = manager.beginTransaction();
                transaction.replace(R.id.fragment_event, itemEventInfoFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case R.id.b_comentari:
                b_comentari.setBackgroundResource(R.drawable.btn_fragment_c);
                b_comentari.setTextColor(Color.WHITE);
                b_info.setBackgroundResource(R.drawable.btn_fragment_u);
                b_info.setTextColor(Color.BLACK);

                ItemEvent_ComentFragment itemEventComentFragment = new ItemEvent_ComentFragment();
                manager = getFragmentManager();
                transaction = manager.beginTransaction();

                transaction.replace(R.id.fragment_event, itemEventComentFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                break;

        }
    }

}
