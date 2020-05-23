package com.example.whattodo.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whattodo.R;
import com.example.whattodo.model.Coment;
import com.example.whattodo.model.Event;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

public class ComentAdapter extends FirestoreRecyclerAdapter<Coment, ComentAdapter.ComentHolder> {

    public ComentAdapter(@NonNull FirestoreRecyclerOptions<Coment> options) {
        super(options);
    }

    public static class ComentHolder extends RecyclerView.ViewHolder {

        public TextView letterUser, mComent;
        ImageView mEstrella1, mEstrella2, mEstrella3, mEstrella4, mEstrella5;

        public ComentHolder(View itemView) {
            super(itemView);
            letterUser = itemView.findViewById(R.id.textUser);
            mComent = itemView.findViewById(R.id.textComentari);
            mEstrella1 = itemView.findViewById(R.id.com_estrella1);
            mEstrella2 = itemView.findViewById(R.id.com_estrella2);
            mEstrella3 = itemView.findViewById(R.id.com_estrella3);
            mEstrella4 = itemView.findViewById(R.id.com_estrella4);
            mEstrella5 = itemView.findViewById(R.id.com_estrella5);
        }
    }

    @Override
    public ComentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_coment, parent, false);
        return new ComentHolder(v);
    }

    @Override
    protected void onBindViewHolder(@NonNull ComentHolder comentHolder, int position, @NonNull Coment coment) {
        comentHolder.letterUser.setText(coment.getUserName());
        comentHolder.mComent.setText(coment.getComentari());
        int puntuacio = coment.getPuntuacio();

        if(puntuacio >= 1) { comentHolder.mEstrella1.setImageResource(R.drawable.estrella_c); }
        if(puntuacio >= 2) { comentHolder.mEstrella2.setImageResource(R.drawable.estrella_c); }
        if(puntuacio >= 3) { comentHolder.mEstrella3.setImageResource(R.drawable.estrella_c); }
        if(puntuacio >= 4) { comentHolder.mEstrella4.setImageResource(R.drawable.estrella_c); }
        if(puntuacio == 5) { comentHolder.mEstrella5.setImageResource(R.drawable.estrella_c); }
    }
}

