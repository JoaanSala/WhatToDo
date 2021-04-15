package com.example.whattodo;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.whattodo.adapter.OfertAdapter;
import com.example.whattodo.model.Ofert;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class OfertFragment extends Fragment{

    private static final String TAG = "OfertFragment";

    View mView;
    TextView text_NOLocation;

    private RecyclerView recyclerView_Ofert;
    FirestoreRecyclerOptions<Ofert> mOptions;
    private OfertAdapter mAdapter;
    private FirebaseFirestore mFirestore;
    private CollectionReference ofertRef;
    FirebaseAuth mAuth;
    String userID;
    MainActivity ma;
    int count = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_ofert, container, false);

        ma = (MainActivity)getActivity();
        boolean locationIsActive = ma.getLocationActivated();

        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        mFirestore = FirebaseFirestore.getInstance();
        ofertRef = mFirestore.collection("oferts");

        recyclerView_Ofert = mView.findViewById(R.id.recyclerview_oferts);
        recyclerView_Ofert.setHasFixedSize(true);
        text_NOLocation = mView.findViewById(R.id.text_NOLocation);

        if(locationIsActive){
            Log.d("LOCATION-OFERT", ma.getLocation());
            recyclerView_Ofert.setVisibility(View.VISIBLE);
            text_NOLocation.setVisibility(View.GONE);
            initRecyclerView();
        }

        return mView;
    }

    private void initRecyclerView() {
        checkDocuments();
        Query query = ofertRef.whereEqualTo("localitzacio", ma.getLocation());

        mOptions = new FirestoreRecyclerOptions.Builder<Ofert>().setQuery(query, Ofert.class).build();
        Log.d("OPTIONS", query.toString());
        mAdapter = new OfertAdapter(mOptions);

        recyclerView_Ofert.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView_Ofert.setAdapter(mAdapter);

        //if(userEdat > 18) {
            mAdapter.setOnItemClickListener(new OfertAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(DocumentSnapshot documentSnapshot, int position) {

                    Fragment payFragment = new OfertFragmentPay();

                    Bundle args = new Bundle();
                    args.putString("document", documentSnapshot.getId());
                    args.putString("title", documentSnapshot.get("title").toString());
                    args.putString("event", documentSnapshot.get("event").toString());
                    args.putString("localitzacio", documentSnapshot.get("localitzacio").toString());
                    args.putString("price", documentSnapshot.get("price").toString());

                    payFragment.setArguments(args);
                    getFragmentManager().beginTransaction().replace(R.id.fragment_main,
                            payFragment).commit();

                }
            });

        //}
    }

    private void checkDocuments() {
        ofertRef.whereEqualTo("localitzacio", ma.getLocation())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        int count = 0;
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){
                                Log.d("Copyin documents", document.getId() + " => " + document.getData());
                                count++;
                            }
                            if(count==0) {
                                text_NOLocation.setVisibility(View.VISIBLE);
                                text_NOLocation.setText("HO SENTIM, PERO EN AQUESTA LOCALITZACIÓ NO TENIM OFERTES EN AQUESTS MOMENTS...");
                            }
                            Log.d("CHECKING-DOCUMENTS", "");
                            Log.d("How many Oferts", String.valueOf(count));
                        }
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        if(ma.getLocationActivated()) {
            mAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(ma.getLocationActivated()) {
            mAdapter.stopListening();
        }
    }

}
