package com.example.whattodo

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.whattodo.adapter.EventAdapter
import com.example.whattodo.model.Event
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class SearchFragment : Fragment(), EventAdapter.OnItemClickListener  {
    private lateinit var mRecyclerView: RecyclerView
    private val mFirestore = FirebaseFirestore.getInstance()
    private val eventRef = mFirestore.collection("entryObject_DB")
    private lateinit var mAdapter :EventAdapter

    private lateinit var viewOfLayout: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewOfLayout = inflater.inflate(R.layout.fragment_search, container, false)
        mRecyclerView = viewOfLayout.findViewById(R.id.recycler_view)

        initRecyclerView()

        val editText = viewOfLayout.findViewById<EditText>(R.id.editText)

        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                filter(editable.toString())
            }
        })
        return viewOfLayout
    }

    private fun initRecyclerView() {
        val mOptions = FirestoreRecyclerOptions.Builder<Event>().setQuery(eventRef, Event::class.java).build()

        mRecyclerView.setHasFixedSize(true)

        mAdapter = EventAdapter(mOptions, this)
        mRecyclerView.layoutManager = LinearLayoutManager(activity)
        mRecyclerView.adapter = mAdapter

    }

    private fun filter(text: String) {
        mAdapter.stopListening()
        val query = eventRef.orderBy("Titol").startAt(text).endAt(text + "\uf8ff")
        val mOptions = FirestoreRecyclerOptions.Builder<Event>().setQuery(query, Event::class.java).build()
        mAdapter = EventAdapter(mOptions, this)
        mRecyclerView.adapter = mAdapter
        mRecyclerView.layoutManager = LinearLayoutManager(context)

        mAdapter.startListening()
    }

    override fun onStart() {
        super.onStart()
        mAdapter.startListening()
        Log.d("onStart", "I'm here!")
    }

    override fun onStop() {
        super.onStop()
        mAdapter.stopListening()
        Log.d("onStop", "I'm here!")
    }

    override fun onItemClick(documentSnapshot: DocumentSnapshot?, position: Int) {
        val documentId = documentSnapshot!!.id
        Log.d("Document-snapshot", documentId)
        Toast.makeText(context, documentSnapshot.get("Titol").toString(), Toast.LENGTH_SHORT)
        val intent = Intent(context, ItemEvent_Activity::class.java)
        intent.putExtra("DOCUMENT_KEY", documentId)
        startActivity(intent)
    }
}