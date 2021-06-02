package com.example.whattodo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.whattodo.adapter.PayCardAdapter
import com.example.whattodo.model.CreditCard
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class PayCardFragment : Fragment() {
    private lateinit var viewOfLayout: View
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var noCreditCards: LinearLayout

    private lateinit var btnAccept: Button
    private lateinit var btnCancell: Button

    private lateinit var adapter: PayCardAdapter
    private lateinit var mFirestore: FirebaseFirestore
    var userID: String? = null
    private lateinit var creditCardQuery: Query

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewOfLayout = inflater.inflate(R.layout.fragment_paycard, container, false)
        mRecyclerView = viewOfLayout.findViewById(R.id.dialog_rv)
        noCreditCards = viewOfLayout.findViewById(R.id.noCreditCard_payied)

        mFirestore = FirebaseFirestore.getInstance()
        userID = FirebaseAuth.getInstance().currentUser.uid

        creditCardQuery = mFirestore.collection("users").document(userID!!).collection("CreditCards")
        //checkDocuments()

        initReciclerView(creditCardQuery)
        return viewOfLayout
    }

    private fun initReciclerView(query: Query) {
        val options = FirestoreRecyclerOptions.Builder<CreditCard>().setQuery(query, CreditCard::class.java).build()
        mRecyclerView!!.setHasFixedSize(true)

        adapter = PayCardAdapter(options)

        mRecyclerView!!.layoutManager = LinearLayoutManager(context)
        mRecyclerView!!.adapter = adapter
    }

    /*
    private fun checkDocuments() {
        creditCardQuery!!.get()
                .addOnCompleteListener { task ->
                    var countNumberCC = 0
                    if (task.isSuccessful) {
                        for (document in task.result!!) {
                            Log.d("Copyin documents", document.id + " => " + document.data)
                            countNumberCC++
                            document.data == "cc_cardVV"
                        }
                        Log.d("How many CreditCards", countNumberCC.toString())
                        if (countNumberCC > 0) {
                            noCreditCards!!.visibility = View.INVISIBLE
                        } else {
                            noCreditCards!!.visibility = View.VISIBLE
                        }
                    }
                }
    }
     */

    override fun onStart() {
        super.onStart()
        adapter.startListening()
        //checkDocuments()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }
}