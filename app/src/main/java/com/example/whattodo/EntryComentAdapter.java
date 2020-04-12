package com.example.whattodo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class EntryComentAdapter extends RecyclerView.Adapter<EntryComentAdapter.EntryComentViewHolder> {

    private ArrayList<EntryComent> entryComentList = new ArrayList<>();
    private int puntuacio;

    public static class EntryComentViewHolder extends RecyclerView.ViewHolder {

        public TextView letterUser, mComent;
        ImageView mEstrella1, mEstrella2, mEstrella3, mEstrella4, mEstrella5;

        public EntryComentViewHolder(View itemView) {
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

    public EntryComentAdapter(ArrayList<EntryComent> comentList){
        this.entryComentList = comentList;

    }

    @Override
    public EntryComentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.entry_coment, parent, false);
        EntryComentViewHolder ecvh = new EntryComentViewHolder(v);
        return ecvh;
    }

    @Override
    public void onBindViewHolder(EntryComentViewHolder holder, int position) {
        EntryComent currentItem = entryComentList.get(position);

        String letterName = currentItem.getUserName().substring(0,1);
        holder.letterUser.setText(letterName);
        holder.mComent.setText(currentItem.getComentari());
        puntuacio = currentItem.getPuntuacio();

        if(puntuacio >= 1) { holder.mEstrella1.setImageResource(R.drawable.estrella_c); }
        if(puntuacio >= 2) { holder.mEstrella2.setImageResource(R.drawable.estrella_c); }
        if(puntuacio >= 3) { holder.mEstrella3.setImageResource(R.drawable.estrella_c); }
        if(puntuacio >= 4) { holder.mEstrella4.setImageResource(R.drawable.estrella_c); }
        if(puntuacio == 5) { holder.mEstrella5.setImageResource(R.drawable.estrella_c); }
    }

    @Override
    public int getItemCount() {
        return entryComentList.size();
    }
}

