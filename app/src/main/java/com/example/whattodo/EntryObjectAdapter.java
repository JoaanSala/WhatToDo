package com.example.whattodo;

import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class EntryObjectAdapter extends RecyclerView.Adapter<EntryObjectAdapter.EntryObjectViewHolder> {
    private ArrayList<EntryObject> mSearchList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }

    public static class EntryObjectViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView mTextView1, mTextView2, mTextView3, mTextView4;

        public EntryObjectViewHolder (View itemView, final OnItemClickListener listener){
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageView);
            mTextView1 = itemView.findViewById(R.id.textView1);
            mTextView2 = itemView.findViewById(R.id.textView2);
            mTextView3 = itemView.findViewById(R.id.textView3);
            mTextView4 = itemView.findViewById(R.id.textView4);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public EntryObjectAdapter(ArrayList<EntryObject> searchList){
        this.mSearchList = searchList;

    }

    @Override
    public EntryObjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.entry_object, parent, false);
        EntryObjectViewHolder eovh = new EntryObjectViewHolder(v, mListener);
        return eovh;
    }

    @Override
    public void onBindViewHolder(EntryObjectViewHolder holder, int position) {
        EntryObject currentItem = mSearchList.get(position);

        holder.mImageView.setImageResource(currentItem.getmImageResource());
        holder.mTextView1.setText(currentItem.getTitol());
        holder.mTextView2.setText(currentItem.getAdreça());
        holder.mTextView3.setText(currentItem.getLocalitzacio());
        holder.mTextView4.setText(currentItem.getQualificacio());
        double color = Double.parseDouble(currentItem.getQualificacio());

        if(color>=8.5){ holder.mTextView4.setTextColor(Color.parseColor("#4CAF50"));}                   //green
        else if(color<8.5 && color>=7){ holder.mTextView4.setTextColor(Color.parseColor("#17B3FA")); }  //blue
        else if(color<7 && color>=6){holder.mTextView4.setTextColor(Color.parseColor("#FFCE1C"));}      //yellow
        else if(color<6 && color>=5){holder.mTextView4.setTextColor(Color.parseColor("#FF8F19"));}      //orange
        else if(color<5){holder.mTextView4.setTextColor(Color.parseColor("#F13123"));}                  //red

    }

    @Override
    public int getItemCount() {
        return mSearchList.size();
    }

    public void filterList(ArrayList<EntryObject> filteredList){
        mSearchList = filteredList;
        notifyDataSetChanged();
    }




}
