package com.example.whattodo;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.whattodo.model.Event;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ItemEvent_InfoFragment extends Fragment implements OnMapReadyCallback{

    private GoogleMap google_Map;
    public MapView mapView;
    public View mView;
    public Button b_afegircoment;

    TextView tv_telefon, tv_detalls, tv_adreça;
    Double mlatitud, mlongitud;
    String titol, telefon, detalls, adreça, ciutat;

    Event event;

    DocumentReference eventItem;
    FirebaseFirestore mFirestore;
    String documentID;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mapView = mView.findViewById(R.id.c_localitzacio_map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_eventitem_info, container, false);
        mFirestore = FirebaseFirestore.getInstance();

        documentID = getActivity().getIntent().getExtras().getString("DOCUMENT_KEY");

        eventItem = mFirestore.collection("entryObject_DB").document(documentID);

        tv_telefon = mView.findViewById(R.id.c_telefon);
        tv_detalls = mView.findViewById(R.id.c_detalls);
        tv_adreça = mView.findViewById(R.id.c_adreça);

        b_afegircoment = mView.findViewById(R.id.b_comentari);
        b_afegircoment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ActivityComent.class);
                intent.putExtra("DOCUMENT_KEY", documentID);
                startActivity(intent);
            }
        });

        return mView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if(googleMap != null) {
            google_Map = googleMap;
        }
        google_Map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        eventItem.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot snapshot) {
                event = snapshot.toObject(Event.class);
                titol = event.getTitol();
                telefon = event.getTelefon();
                detalls = event.getDetalls();
                adreça = event.getAdreça();
                ciutat = event.getCiutat();

                tv_telefon.setText(telefon);
                tv_detalls.setText(detalls);
                tv_adreça.setText(adreça + ", " + ciutat);

                mlatitud = event.getLatitud();
                mlongitud = event.getLongitud();

                LatLng location = new LatLng(mlatitud, mlongitud);
                google_Map.addMarker(new MarkerOptions().position(location).title(titol));
                google_Map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15.5f));
            }
        });

    }
}
