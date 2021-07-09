package com.example.whattodo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.whattodo.adapter.EventAdapter
import com.example.whattodo.model.Event
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class EventFragment : Fragment(), EventAdapter.OnItemClickListener {

    private lateinit var mRecyclerView: RecyclerView
    var mView: View? = null
    var message: String? = null
    private lateinit var mFirestore: FirebaseFirestore
    private lateinit var eventRef: Query
    private lateinit var adapter: EventAdapter
    private lateinit var viewOfLayout: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewOfLayout = inflater.inflate(R.layout.fragment_event, container, false)

        mRecyclerView = viewOfLayout.findViewById(R.id.recycler_view_events)
        mFirestore = FirebaseFirestore.getInstance()
        eventRef = mFirestore.collection("entryObject_DB")

        val bundle = arguments
        message = bundle!!.getString("Type_Event")
        val titol = viewOfLayout.findViewById<TextView>(R.id.textEvent)
        titol.text = message
        initRecyclerView()

        val back = viewOfLayout.findViewById<ImageView>(R.id.arrow_back)
        back.setOnClickListener { requireFragmentManager().beginTransaction().replace(R.id.fragment_main, HomeFragment()).commit() }

        return viewOfLayout
    }

    private fun initRecyclerView() {
        val query = eventRef.whereEqualTo("TypeID", message)
        val options: FirestoreRecyclerOptions<Event> = FirestoreRecyclerOptions.Builder<Event>().setQuery(query, Event::class.java).build()
        mRecyclerView.setHasFixedSize(true)

        adapter = EventAdapter(options, this)

        mRecyclerView.layoutManager = LinearLayoutManager(context)
        mRecyclerView.adapter = adapter

    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    override fun onItemClick(documentSnapshot: DocumentSnapshot?, position: Int) {
        val documentId = documentSnapshot?.id
        val intent = Intent(context, ItemEvent_Activity::class.java)
        intent.putExtra("DOCUMENT_KEY", documentId)
        startActivity(intent)
    }
}