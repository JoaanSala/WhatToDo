package com.example.whattodo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.whattodo.adapter.ComentAdapter
import com.example.whattodo.model.Coment
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class ItemEvent_ComentFragment : Fragment() {
    private lateinit var viewOfLayout: View
    private lateinit var mRecyclerView: RecyclerView

    private var adapter: ComentAdapter? = null
    private lateinit var mFirestore: FirebaseFirestore
    private lateinit var eventRef: DocumentReference

    var documentID: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewOfLayout = inflater.inflate(R.layout.fragment_eventitem_coment, container, false)

        mRecyclerView = viewOfLayout.findViewById(R.id.coment_recycler_view)
        mFirestore = FirebaseFirestore.getInstance()

        documentID = activity!!.intent.extras!!.getString("DOCUMENT_KEY")

        eventRef = mFirestore.collection("entryObject_DB").document(documentID!!)
        val query = eventRef.collection("coment").limit(10)

        initRecyclerView(query)
        return viewOfLayout
    }

    private fun initRecyclerView(query: Query) {
        val options = FirestoreRecyclerOptions.Builder<Coment>().setQuery(query, Coment::class.java).build()
        mRecyclerView.setHasFixedSize(true)

        adapter = ComentAdapter(options)

        mRecyclerView.layoutManager = LinearLayoutManager(context)
        mRecyclerView.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        adapter!!.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter!!.stopListening()
    }
}