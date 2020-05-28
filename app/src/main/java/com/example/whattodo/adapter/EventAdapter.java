package com.example.whattodo.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.whattodo.R;
import com.example.whattodo.model.Event;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class EventAdapter extends FirestoreRecyclerAdapter<Event, EventAdapter.EventHolder> {
    private OnItemClickListener mListener;

    public EventAdapter(@NonNull FirestoreRecyclerOptions<Event> options) {
        super(options);
    }

    public interface OnItemClickListener{
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }

    public class EventHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView mTextView1, mTextView2, mTextView3, mTextView4;

        public EventHolder (View itemView, final OnItemClickListener listener){
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageView);
            mTextView1 = itemView.findViewById(R.id.textView1);
            mTextView2 = itemView.findViewById(R.id.textView2);
            mTextView3 = itemView.findViewById(R.id.textView3);
            mTextView4 = itemView.findViewById(R.id.textView4);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION && mListener != null){
                        mListener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });
        }

        public void bind(Event object) {
            Glide.with(mImageView.getContext())
                    .load(object.getURL())
                    .into(mImageView);
        }
    }

    @Override
    public EventHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event, parent, false);
        return new EventHolder(v, mListener);
    }


    @Override
    protected void onBindViewHolder(@NonNull EventHolder eventHolder, int position, @NonNull Event event) {
        eventHolder.bind(event);
        eventHolder.mTextView1.setText(event.getTitol());
        eventHolder.mTextView2.setText(event.getAdreça());
        eventHolder.mTextView3.setText(event.getLocalitzacio());
        eventHolder.mTextView4.setText(event.getQualificacio());
        double color = Double.parseDouble(event.getQualificacio());

        if(color>=8.5){ eventHolder.mTextView4.setTextColor(Color.parseColor("#4CAF50"));}                   //green
        else if(color<8.5 && color>=7){ eventHolder.mTextView4.setTextColor(Color.parseColor("#17B3FA")); }  //blue
        else if(color<7 && color>=6){eventHolder.mTextView4.setTextColor(Color.parseColor("#FFCE1C"));}      //yellow
        else if(color<6 && color>=5){eventHolder.mTextView4.setTextColor(Color.parseColor("#FF8F19"));}      //orange
        else if(color<5){eventHolder.mTextView4.setTextColor(Color.parseColor("#F13123"));}                  //red

    }
}
