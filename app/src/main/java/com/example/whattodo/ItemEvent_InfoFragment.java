package com.example.whattodo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ItemEvent_InfoFragment extends Fragment implements OnMapReadyCallback {
    static final int PICK_CONTACT_REQUEST = 1;  // The request code

    private GoogleMap google_Map;
    public MapView mapView;
    public View mView;
    public Button b_afegircoment;

    TextView tv_telefon, tv_detalls, tv_adreça;
    Double mlatitud, mlongitud;
    String titol, telefon, detalls, adreça, ciutat;

    EntryObject entryObject;

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
        Log.i("InfoFragment", "onCreateView");
        Bundle bundle = getArguments();

        if(bundle != null) {
            entryObject = bundle.getParcelable("Item_Object");
        }else{
            entryObject = ((ItemEvent_Fragment) getParentFragment()).getEntryObject();
        }

        titol = entryObject.getTitol();
        telefon = entryObject.getTelefon();
        detalls = entryObject.getDetalls();
        adreça = entryObject.getAdreça();
        ciutat = entryObject.getCiutat();

        tv_telefon = mView.findViewById(R.id.c_telefon);
        tv_detalls = mView.findViewById(R.id.c_detalls);
        tv_adreça = mView.findViewById(R.id.c_adreça);

        tv_telefon.setText(telefon);
        tv_detalls.setText(detalls);
        tv_adreça.setText(adreça + ", " + ciutat);

        mlatitud = entryObject.getLatitud();
        mlongitud = entryObject.getLongitud();

        b_afegircoment = mView.findViewById(R.id.b_comentari);
        b_afegircoment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getActivity(), ActivityComent.class), PICK_CONTACT_REQUEST);
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

        LatLng location = new LatLng(mlatitud, mlongitud);
        this.google_Map.addMarker(new MarkerOptions().position(location).title(titol));
        this.google_Map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15.5f));
    }
}
