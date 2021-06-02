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
    private lateinit var b_afegircoment: Button
    var tv_telefon: TextView? = null
    var tv_detalls: TextView? = null
    var tv_adreça: TextView? = null
    var mlatitud: Double? = null
    var mlongitud: Double? = null
    var titol: String? = null
    var telefon: String? = null
    var detalls: String? = null
    var adreça: String? = null
    var ciutat: String? = null
    var event: Event? = null
    var eventItem: DocumentReference? = null
    var mFirestore: FirebaseFirestore? = null
    var documentID: String? = null

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

        documentID = activity!!.intent.extras!!.getString("DOCUMENT_KEY")
        eventItem = mFirestore!!.collection("entryObject_DB").document(documentID!!)
        tv_telefon = viewOfLayout.findViewById(R.id.c_telefon)
        tv_detalls = viewOfLayout.findViewById(R.id.c_detalls)
        tv_adreça = viewOfLayout.findViewById(R.id.c_adreça)

        b_afegircoment = viewOfLayout.findViewById(R.id.b_comentari)
        b_afegircoment.setOnClickListener(View.OnClickListener {
            val intent = Intent(context, ActivityComent::class.java)
            intent.putExtra("DOCUMENT_KEY", documentID)
            startActivity(intent)
        })

        return viewOfLayout
    }

    override fun onMapReady(googleMap: GoogleMap) {
        if (googleMap != null) {
            google_Map = googleMap
        }
        google_Map!!.mapType = GoogleMap.MAP_TYPE_NORMAL
        eventItem!!.get().addOnSuccessListener { snapshot ->
            event = snapshot.toObject(Event::class.java)
            titol = event!!.Titol
            telefon = event!!.Telefon
            detalls = event!!.Detalls
            adreça = event!!.Adreça
            ciutat = event!!.Ciutat
            tv_telefon!!.text = telefon
            tv_detalls!!.text = detalls
            tv_adreça!!.text = "$adreça, $ciutat"
            mlatitud = event!!.Latitud
            mlongitud = event!!.Longitud
            val location = LatLng(mlatitud!!, mlongitud!!)
            google_Map!!.addMarker(MarkerOptions().position(location).title(titol))
            google_Map!!.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15.5f))
        }
    }
}