package com.example.whattodo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.whattodo.adapter.OfertAdapter
import com.example.whattodo.model.Ofert
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class OfertFragment : Fragment(), OfertAdapter.OnItemClickListener {
    private lateinit var viewOfLayout: View

    private lateinit var text_NOLocation: TextView
    private lateinit var recyclerView_Ofert: RecyclerView
    private lateinit var mOptions: FirestoreRecyclerOptions<Ofert>
    private lateinit var mAdapter: OfertAdapter
    private lateinit var mFirestore: FirebaseFirestore
    private var ofertRef: CollectionReference? = null
    private lateinit var mAuth: FirebaseAuth
    var userID: String? = null
    private lateinit var ma: MainActivity
    var count = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewOfLayout = inflater.inflate(R.layout.fragment_ofert, container, false)
        recyclerView_Ofert = viewOfLayout.findViewById(R.id.recyclerview_oferts)

        ma = activity as MainActivity
        val locationIsActive = ma.locationActivated
        mAuth = FirebaseAuth.getInstance()
        userID = mAuth.currentUser!!.uid
        mFirestore = FirebaseFirestore.getInstance()
        ofertRef = mFirestore.collection("oferts")


        text_NOLocation = viewOfLayout.findViewById(R.id.text_NOLocation)

        if (locationIsActive) {
            Log.d("LOCATION-OFERT", ma!!.location!!)
            recyclerView_Ofert.setVisibility(View.VISIBLE)
            text_NOLocation.setVisibility(View.GONE)
            initRecyclerView()
        }

        return viewOfLayout
    }

    private fun initRecyclerView() {
        val query = ofertRef!!.whereEqualTo("localitzacio", ma!!.location)
        mOptions = FirestoreRecyclerOptions.Builder<Ofert>().setQuery(query, Ofert::class.java).build()
        recyclerView_Ofert.setHasFixedSize(true)

        mAdapter = OfertAdapter(mOptions, this)
        recyclerView_Ofert.layoutManager = LinearLayoutManager(activity)
        recyclerView_Ofert.adapter = mAdapter

    }


    override fun onItemClick(documentSnapshot: DocumentSnapshot?, position: Int) {
        val documentId = documentSnapshot?.id

        val intent= Intent(context, OfertActivityPay::class.java)

        intent.putExtra("document", documentSnapshot?.id)
        intent.putExtra("title", documentSnapshot!!["title"].toString())
        intent.putExtra("event", documentSnapshot["event"].toString())
        intent.putExtra("localitzacio", documentSnapshot["localitzacio"].toString())
        intent.putExtra("price", documentSnapshot["price"].toString())

        startActivity(intent)
    }


    private fun checkDocuments() {
        ofertRef!!.whereEqualTo("localitzacio", ma!!.location)
                .get()
                .addOnCompleteListener { task ->
                    var count = 0
                    if (task.isSuccessful) {
                        for (document in task.result!!) {
                            Log.d("Copyin documents", document.id + " => " + document.data)
                            count++
                        }
                        if (count == 0) {
                            text_NOLocation!!.visibility = View.VISIBLE
                            text_NOLocation!!.text = "HO SENTIM, PERO EN AQUESTA LOCALITZACIÓ NO TENIM OFERTES EN AQUESTS MOMENTS..."
                        }
                        Log.d("CHECKING-DOCUMENTS", "")
                        Log.d("How many Oferts", count.toString())
                    }
                }
    }


    override fun onStart() {
        super.onStart()
        if (ma.locationActivated) {
            mAdapter.startListening()
        }
    }

    override fun onStop() {
        super.onStop()
        if (ma.locationActivated) {
            mAdapter.stopListening()
        }
    }

    companion object {
        private const val TAG = "OfertFragment"
    }
}