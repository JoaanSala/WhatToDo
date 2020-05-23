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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.whattodo.model.Event;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ItemEvent_Fragment extends Fragment implements View.OnClickListener{

    ImageView image_background;
    TextView b_close, tv_title_event, tv_localitzacio;
    String titol, localitzacio;
    Button b_info, b_comentari;
    FragmentTransaction transaction;
    View mView;

    DocumentReference eventItem;
    FirebaseFirestore mFirestore;
    Event event;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_eventitem, container, false);

        mFirestore = FirebaseFirestore.getInstance();

        image_background = mView.findViewById(R.id.image_eventItem);
        tv_title_event = mView.findViewById(R.id.title_event);
        tv_localitzacio = mView.findViewById(R.id.localitzacio);

        String documentID = getActivity().getIntent().getExtras().getString("DOCUMENT_KEY");

        eventItem = mFirestore.collection("entryObject_DB").document(documentID);

        eventItem.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                event = documentSnapshot.toObject(Event.class);

                Glide.with(image_background.getContext())
                        .load(event.getURL())
                        .centerCrop()
                        .into(image_background);

                titol = event.getTitol();
                localitzacio = event.getLocalitzacio();

                tv_title_event.setText(titol);
                tv_localitzacio.setText(localitzacio);
            }
        });

        b_close = mView.findViewById(R.id.close_activity);
        b_close.setOnClickListener(this);


        b_info = mView.findViewById(R.id.b_info);
        b_comentari = mView.findViewById(R.id.b_comentari);
        b_info.setOnClickListener(this);
        b_comentari.setOnClickListener(this);

        return mView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.close_activity:
                getActivity().onBackPressed();
                break;

            case R.id.b_info:
                b_comentari.setBackgroundResource(R.drawable.btn_fragment_u);
                b_comentari.setTextColor(Color.BLACK);
                b_info.setBackgroundResource(R.drawable.btn_fragment_c);
                b_info.setTextColor(Color.WHITE);

                getFragmentManager().beginTransaction().replace(R.id.fragment_event, new ItemEvent_InfoFragment()).commit();
                break;
            case R.id.b_comentari:
                b_comentari.setBackgroundResource(R.drawable.btn_fragment_c);
                b_comentari.setTextColor(Color.WHITE);
                b_info.setBackgroundResource(R.drawable.btn_fragment_u);
                b_info.setTextColor(Color.BLACK);

                transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_event, new ItemEvent_ComentFragment(), "");
                transaction.commit();
                break;
        }
    }
}
