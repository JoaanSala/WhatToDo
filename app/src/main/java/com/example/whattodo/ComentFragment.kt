package com.example.whattodo

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.whattodo.model.Coment
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class ComentFragment : Fragment(), View.OnClickListener {
    private lateinit var mEstrella1: ImageView
    private lateinit var mEstrella2: ImageView
    private lateinit var mEstrella3: ImageView
    private lateinit var mEstrella4: ImageView
    private lateinit var mEstrella5: ImageView

    var estrellaValue = 0
    private lateinit var mPublica: Button

    var mComentari: EditText? = null
    private lateinit var viewOfLayout: View
    var mAuth: FirebaseAuth? = null
    var mStore: FirebaseFirestore? = null
    var documentID: String? = null
    var userID: String? = null
    var userName: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewOfLayout = inflater.inflate(R.layout.fragment_coment, container, false)

        mEstrella1 = viewOfLayout.findViewById(R.id.estrella1)
        mEstrella2 = viewOfLayout.findViewById(R.id.estrella2)
        mEstrella3 = viewOfLayout.findViewById(R.id.estrella3)
        mEstrella4 = viewOfLayout.findViewById(R.id.estrella4)
        mEstrella5 = viewOfLayout.findViewById(R.id.estrella5)

        mAuth = FirebaseAuth.getInstance()
        mStore = FirebaseFirestore.getInstance()
        userID = mAuth!!.currentUser.uid
        documentID = activity!!.intent.extras!!.getString("DOCUMENT_KEY")
        mComentari = viewOfLayout.findViewById(R.id.com_comentari)
        mPublica = viewOfLayout.findViewById(R.id.publica_comentari)

        mEstrella1.setOnClickListener(this)
        mEstrella2.setOnClickListener(this)
        mEstrella3.setOnClickListener(this)
        mEstrella4.setOnClickListener(this)
        mEstrella5.setOnClickListener(this)
        mPublica.setOnClickListener(this)

        val backarrow = viewOfLayout.findViewById<ImageView>(R.id.coment_back_arrow)
        backarrow.setOnClickListener(this)

        return viewOfLayout
    }

    override fun onClick(view: View) {
        var returnIntent: Intent
        when (view.id) {
            R.id.estrella1 -> {
                mEstrella1.setBackgroundResource(R.drawable.estrella_c)
                mEstrella2.setBackgroundResource(R.drawable.estrella_u)
                mEstrella3.setBackgroundResource(R.drawable.estrella_u)
                mEstrella4.setBackgroundResource(R.drawable.estrella_u)
                mEstrella5.setBackgroundResource(R.drawable.estrella_u)
                estrellaValue = 1
            }
            R.id.estrella2 -> {
                mEstrella1.setBackgroundResource(R.drawable.estrella_c)
                mEstrella2.setBackgroundResource(R.drawable.estrella_c)
                mEstrella3.setBackgroundResource(R.drawable.estrella_u)
                mEstrella4.setBackgroundResource(R.drawable.estrella_u)
                mEstrella5.setBackgroundResource(R.drawable.estrella_u)
                estrellaValue = 2
            }
            R.id.estrella3 -> {
                mEstrella1.setBackgroundResource(R.drawable.estrella_c)
                mEstrella2.setBackgroundResource(R.drawable.estrella_c)
                mEstrella3.setBackgroundResource(R.drawable.estrella_c)
                mEstrella4.setBackgroundResource(R.drawable.estrella_u)
                mEstrella5.setBackgroundResource(R.drawable.estrella_u)
                estrellaValue = 3
            }
            R.id.estrella4 -> {
                mEstrella1.setBackgroundResource(R.drawable.estrella_c)
                mEstrella2.setBackgroundResource(R.drawable.estrella_c)
                mEstrella3.setBackgroundResource(R.drawable.estrella_c)
                mEstrella4.setBackgroundResource(R.drawable.estrella_c)
                mEstrella5.setBackgroundResource(R.drawable.estrella_u)
                estrellaValue = 4
            }
            R.id.estrella5 -> {
                mEstrella1.setBackgroundResource(R.drawable.estrella_c)
                mEstrella2.setBackgroundResource(R.drawable.estrella_c)
                mEstrella3.setBackgroundResource(R.drawable.estrella_c)
                mEstrella4.setBackgroundResource(R.drawable.estrella_c)
                mEstrella5.setBackgroundResource(R.drawable.estrella_c)
                estrellaValue = 5
            }
            R.id.publica_comentari -> {

                //getUserLetter();
                val documentReference = mStore!!.collection("users").document(userID!!)
                documentReference.addSnapshotListener { snapshot, e ->
                    val userName = snapshot!!.getString("name")
                    val userSurname = snapshot.getString("surname")
                    val letter = Character.toString(userName!![0]) + Character.toString(userSurname!![0])
                    val event = mStore!!.collection("entryObject_DB").document(documentID!!)
                    val coment = Coment(letter, mComentari!!.text.toString(), estrellaValue)
                    addRating(event, coment)
                }
                activity!!.finish()
            }
            R.id.coment_back_arrow -> activity!!.finish()
        }
    }

    private fun addRating(eventRef: DocumentReference, coment: Coment): Task<Void> {
        val comentRef = eventRef.collection("coment")
                .document()
        return mStore!!.runTransaction { transaction ->
            transaction[comentRef] = coment
            null
        }
    }
}