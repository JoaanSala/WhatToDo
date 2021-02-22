package com.example.whattodo.adapter;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.whattodo.PayCardDialogFragment;
import com.example.whattodo.R;
import com.example.whattodo.model.Ofert;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;



public class OfertAdapter extends FirestoreRecyclerAdapter<Ofert, OfertAdapter.OfertHolder> {
    private OfertAdapter.OnItemClickListener mListener;

    public OfertAdapter(@NonNull FirestoreRecyclerOptions<Ofert> options) {
        super(options);
    }

    public interface OnItemClickListener{
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(OfertAdapter.OnItemClickListener listener){
        this.mListener = listener;
    }

    public class OfertHolder extends RecyclerView.ViewHolder {
        public Button bt_getOfert;
        public TextView eventANDlocation, title, caducitat, duration;
        public RelativeLayout ofertaAdquired;
        public ImageView imageOfert;

        public OfertHolder(@NonNull View itemView, final OfertAdapter.OnItemClickListener listener) {
            super(itemView);
            imageOfert = itemView.findViewById(R.id.image_ofert);
            bt_getOfert = itemView.findViewById(R.id.button_adquirir);
            eventANDlocation = itemView.findViewById(R.id.event_and_location);
            title = itemView.findViewById(R.id.ofert_title);
            caducitat = itemView.findViewById(R.id.text_caducitat);
            duration = itemView.findViewById(R.id.text_duration);
            ofertaAdquired = itemView.findViewById(R.id.oferta_adquirida);

            bt_getOfert.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int position = getAdapterPosition();
                    bt_getOfert.setVisibility(View.GONE);
                    ofertaAdquired.setVisibility(View.VISIBLE);
                    if(position != RecyclerView.NO_POSITION && mListener != null){
                        mListener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });
        }

        public void bind(final Ofert ofert, int position) {
            Glide.with(imageOfert.getContext())
                    .load(ofert.getPhoto())
                    .into(imageOfert);

            String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            final DocumentReference ofertDocument = FirebaseFirestore.getInstance().collection("users").document(userID).collection("PaidOferts").document(getSnapshots().getSnapshot(position).getId());

            ofertDocument.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                    if (snapshot.getBoolean("acquired") != null) {
                        if (snapshot.getBoolean("acquired") == true) {
                            bt_getOfert.setVisibility(View.GONE);
                            ofertaAdquired.setVisibility(View.VISIBLE);
                        } else {
                            bt_getOfert.setVisibility(View.VISIBLE);
                            ofertaAdquired.setVisibility(View.GONE);
                            Log.d("OFERTID", "This ofert isn't adquire!");
                        }
                    }
                }
            });
        }
    }

    @Override
    public OfertHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ofert, parent, false);
        return new OfertHolder(v, mListener);
    }

    @Override
    protected void onBindViewHolder(@NonNull OfertHolder ofertHolder, int i, @NonNull Ofert ofert) {
        ofertHolder.bind(ofert, i);
        ofertHolder.eventANDlocation.setText(ofert.getEvent()+", "+ ofert.getLocalitzacio());
        ofertHolder.title.setText(ofert.getTitle());
        ofertHolder.caducitat.setText("Oferta Vàlida fins "+ofert.getValidesa());

        String getDate = ofert.getValidesa()+"/2020";
        DateTimeFormatter sdf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate futureDate = LocalDate.parse(getDate, sdf);
        LocalDate presentDate = LocalDate.now();
        Period period = Period.between(presentDate, futureDate);
        ofertHolder.duration.setText("Finalitza en:  "+period.getMonths()+" Mesos -"+period.getDays()+" Dies");
    }
}
