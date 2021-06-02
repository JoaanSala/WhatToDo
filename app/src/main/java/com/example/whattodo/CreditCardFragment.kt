package com.example.whattodo

import android.content.Intent
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
import com.example.whattodo.adapter.CreditCardAdapter
import com.example.whattodo.model.CreditCard
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class CreditCardFragment : Fragment(), View.OnClickListener {

    var noCreditCards: LinearLayout? = null
    private lateinit var mRecyclerView: RecyclerView
    private var adapter: CreditCardAdapter? = null
    private lateinit var mFirestore: FirebaseFirestore
    var userID: String? = null
    private lateinit var creditCardQuery: Query

    var stCards: List<CreditCard>? = null

    private lateinit var viewOfLayout: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewOfLayout = inflater.inflate(R.layout.fragment_creditcard_menu, container, false)

        mRecyclerView = viewOfLayout.findViewById(R.id.creditCard_recycler_view)
        val newCard = viewOfLayout.findViewById<Button>(R.id.btnNew)
        val deleteCards = viewOfLayout.findViewById<Button>(R.id.btnDelete)
        noCreditCards = viewOfLayout.findViewById(R.id.noCreditCard)
        mFirestore = FirebaseFirestore.getInstance()
        userID = FirebaseAuth.getInstance().currentUser.uid
        creditCardQuery = mFirestore.collection("users").document(userID!!).collection("CreditCards")

        newCard.setOnClickListener(this)
        deleteCards.setOnClickListener(this)

        checkDocuments()
        initRecyclerView(creditCardQuery)

        return viewOfLayout
    }

    private fun initRecyclerView(query: Query) {
        val options = FirestoreRecyclerOptions.Builder<CreditCard>().setQuery(query, CreditCard::class.java).build()

        mRecyclerView?.setHasFixedSize(true)
        mRecyclerView?.layoutManager = LinearLayoutManager(context)

        adapter = CreditCardAdapter(options)
        mRecyclerView.adapter = adapter
    }

    fun updateCreditCards(documentID: String?, numTarjeta: String?) {
        mFirestore!!.collection("users").document(userID!!).collection("CreditCards").document(documentID!!)
                .delete().addOnSuccessListener { Log.d("DELETE CREDIT CARD", "TARJETA: $numTarjeta ELIMINADA") }
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btnNew -> {
                val intent = Intent(activity, CC_ItemCreditCardActivity::class.java)
                startActivity(intent)
            }
            R.id.btnDelete ->                 //adapter.getCreditCards();
            {
                var i = 0
                while (i < adapter!!.itemCount) {
                    val creditCard = adapter!!.getItem(i)
                    if (creditCard.isSelected == true) {
                        Log.d("NUM-TARGETA-SELECCIONADA", creditCard.numTargeta)
                        updateCreditCards(adapter!!.snapshots.getSnapshot(i).id, creditCard.numTargeta)
                    }
                    i++
                }
            }
        }
    }

    private fun checkDocuments() {
        creditCardQuery!!.get()
                .addOnCompleteListener { task ->
                    var countNumberCC = 0
                    if (task.isSuccessful) {
                        for (document in task.result!!) {
                            Log.d("Copyin documents", document.id + " => " + document.data)
                            countNumberCC++
                            document.data.equals("cc_cardVV")
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

    override fun onStart() {
        super.onStart()
        adapter!!.startListening()
        checkDocuments()
    }

    override fun onStop() {
        super.onStop()
        adapter!!.stopListening()
    }
}