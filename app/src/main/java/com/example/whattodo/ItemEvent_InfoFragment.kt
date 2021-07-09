package com.example.whattodo

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.whattodo.model.Event
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class ItemEvent_InfoFragment : Fragment(), OnMapReadyCallback {
    private var google_Map: GoogleMap? = null
    private lateinit var mapView: MapView
    private lateinit var viewOfLayout: View

    private lateinit var tv_telefon: TextView
    private lateinit var tv_detalls: TextView
    private lateinit var tv_adreça: TextView
    private var mlatitud: Double? = null
    private var mlongitud: Double? = null
    private lateinit var titol: String
    private lateinit var telefon: String
    private lateinit var detalls: String
    private lateinit var adreça: String
    private lateinit var ciutat: String
    private lateinit var event: Event

    private lateinit var eventItem: DocumentReference

    private lateinit var mFirestore: FirebaseFirestore
    private lateinit var documentID: String

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mapView = viewOfLayout.findViewById(R.id.c_localitzacio_map)
        mapView.onCreate(savedInstanceState)
        mapView.onResume()
        mapView.getMapAsync(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewOfLayout = inflater.inflate(R.layout.fragment_eventitem_info, container, false)
        mFirestore = FirebaseFirestore.getInstance()

        documentID = requireActivity().intent.getStringExtra("DOCUMENT_KEY")!!
        eventItem = mFirestore!!.collection("entryObject_DB").document(documentID!!)
        tv_telefon = viewOfLayout.findViewById(R.id.c_telefon)
        tv_detalls = viewOfLayout.findViewById(R.id.c_detalls)
        tv_adreça = viewOfLayout.findViewById(R.id.c_adreça)

        return viewOfLayout
    }

    override fun onMapReady(googleMap: GoogleMap) {
        if (googleMap != null) {
            google_Map = googleMap
        }
        google_Map!!.mapType = GoogleMap.MAP_TYPE_NORMAL
        eventItem.get().addOnSuccessListener { snapshot ->
            event = snapshot.toObject(Event::class.java)!!
            titol = event.Titol.toString()
            telefon = event.Telefon.toString()
            detalls = event.Detalls.toString()
            adreça = event.Adreça.toString()
            ciutat = event.Ciutat.toString()
            tv_telefon.text = telefon
            tv_detalls.text = detalls
            tv_adreça.text = "$adreça, $ciutat"
            mlatitud = event.Latitud!!
            mlongitud = event.Longitud!!
            val location = LatLng(mlatitud!!, mlongitud!!)
            google_Map!!.addMarker(MarkerOptions().position(location).title(titol))
            google_Map!!.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15.5f))
        }
    }
}