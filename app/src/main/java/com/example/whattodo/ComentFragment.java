package com.example.whattodo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.whattodo.model.Coment;
import com.example.whattodo.model.Event;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.Transaction;

import org.w3c.dom.Document;

import java.util.concurrent.Executor;

public class ComentFragment extends Fragment implements View.OnClickListener {

    ImageView mEstrella1, mEstrella2, mEstrella3, mEstrella4, mEstrella5;
    int estrellaValue = 0;
    Button mPublica;
    EditText mComentari;
    View mView;

    FirebaseAuth mAuth;
    FirebaseFirestore mStore;
    String documentID, userID;
    String userName;

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

        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();
        userID = mAuth.getCurrentUser().getUid();

        documentID = getActivity().getIntent().getExtras().getString("DOCUMENT_KEY");

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

                //getUserLetter();
                DocumentReference documentReference = mStore.collection("users").document(userID);
                documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                        String userName = snapshot.getString("name");
                        String userSurname = snapshot.getString("surname");
                        String letter = Character.toString(userName.charAt(0))+Character.toString(userSurname.charAt(0));
                        DocumentReference event = mStore.collection("entryObject_DB").document(documentID);
                        Coment coment = new Coment(letter, mComentari.getText().toString(),estrellaValue);
                        addRating(event, coment);
                    }
                });
                getActivity().finish();
                break;

            case R.id.coment_back_arrow:
                getActivity().finish();
                break;
        }
    }

    private Task<Void> addRating(final DocumentReference eventRef, final Coment coment) {

        final DocumentReference comentRef = eventRef.collection("coment")
                .document();

        return mStore.runTransaction(new Transaction.Function<Void>() {
            @Nullable
            @Override
            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                transaction.set(comentRef, coment);
                return null;
            }
        });

    }
}
