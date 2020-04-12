package com.example.whattodo;

import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.Map;

public class ComentFragment extends Fragment implements View.OnClickListener {

    ImageView mEstrella1, mEstrella2, mEstrella3, mEstrella4, mEstrella5;
    int estrellaValue = 0;
    Button mPublica;
    EditText mComentari;
    EntryComent_DB coment_db;
    View mView;
    Bundle bundle;
    EntryObject entryObject;
    String message;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_coment, container, false);

        mEstrella1 = mView.findViewById(R.id.estrella1);
        mEstrella2 = mView.findViewById(R.id.estrella2);
        mEstrella3 = mView.findViewById(R.id.estrella3);
        mEstrella4 = mView.findViewById(R.id.estrella4);
        mEstrella5 = mView.findViewById(R.id.estrella5);

        mEstrella1.setOnClickListener(this);
        mEstrella2.setOnClickListener(this);
        mEstrella3.setOnClickListener(this);
        mEstrella4.setOnClickListener(this);
        mEstrella5.setOnClickListener(this);


        mComentari = mView.findViewById(R.id.com_comentari);
        mPublica = mView.findViewById(R.id.publica_comentari);
        mPublica.setOnClickListener(this);

        ImageView backarrow = mView.findViewById(R.id.coment_back_arrow);
        backarrow.setOnClickListener(this);

        return mView;
    }

    @Override
    public void onClick(View view) {
        Intent returnIntent;
        switch (view.getId()) {
            case R.id.estrella1:
                mEstrella1.setBackgroundResource(R.drawable.estrella_c);
                mEstrella2.setBackgroundResource(R.drawable.estrella_u);
                mEstrella3.setBackgroundResource(R.drawable.estrella_u);
                mEstrella4.setBackgroundResource(R.drawable.estrella_u);
                mEstrella5.setBackgroundResource(R.drawable.estrella_u);
                estrellaValue = 1;
                break;
            case R.id.estrella2:
                mEstrella1.setBackgroundResource(R.drawable.estrella_c);
                mEstrella2.setBackgroundResource(R.drawable.estrella_c);
                mEstrella3.setBackgroundResource(R.drawable.estrella_u);
                mEstrella4.setBackgroundResource(R.drawable.estrella_u);
                mEstrella5.setBackgroundResource(R.drawable.estrella_u);
                estrellaValue = 2;
                break;
            case R.id.estrella3:
                mEstrella1.setBackgroundResource(R.drawable.estrella_c);
                mEstrella2.setBackgroundResource(R.drawable.estrella_c);
                mEstrella3.setBackgroundResource(R.drawable.estrella_c);
                mEstrella4.setBackgroundResource(R.drawable.estrella_u);
                mEstrella5.setBackgroundResource(R.drawable.estrella_u);
                estrellaValue = 3;
                break;
            case R.id.estrella4:
                mEstrella1.setBackgroundResource(R.drawable.estrella_c);
                mEstrella2.setBackgroundResource(R.drawable.estrella_c);
                mEstrella3.setBackgroundResource(R.drawable.estrella_c);
                mEstrella4.setBackgroundResource(R.drawable.estrella_c);
                mEstrella5.setBackgroundResource(R.drawable.estrella_u);
                estrellaValue = 4;
                break;
            case R.id.estrella5:
                mEstrella1.setBackgroundResource(R.drawable.estrella_c);
                mEstrella2.setBackgroundResource(R.drawable.estrella_c);
                mEstrella3.setBackgroundResource(R.drawable.estrella_c);
                mEstrella4.setBackgroundResource(R.drawable.estrella_c);
                mEstrella5.setBackgroundResource(R.drawable.estrella_c);
                estrellaValue = 5;
                break;

            case R.id.publica_comentari:
                /*EntryComent_DB entryComentDb = new EntryComent_DB();
                String comentari = mComentari.getText().toString();
                entryComentDb.addNewComent("Pere", comentari, estrellaValue);*/
                getActivity().finish();
                break;

            case R.id.coment_back_arrow:
                getActivity().finish();
                break;
        }
    }
}
