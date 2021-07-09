package com.example.whattodo

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.Glide
import com.example.whattodo.model.Event
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class ItemEvent_Fragment : Fragment(), View.OnClickListener {

    private lateinit var image_background: ImageView
    private lateinit var b_close: TextView
    private lateinit var tv_title_event: TextView
    private lateinit var tv_localitzacio: TextView
    var titol: String? = null
    var localitzacio: String? = null
    private lateinit var b_info: Button
    private lateinit var b_comentari: Button
    var transaction: FragmentTransaction? = null
    var eventItem: DocumentReference? = null
    var mFirestore: FirebaseFirestore? = null
    var event: Event? = null

    private lateinit var viewOfLayout: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewOfLayout = inflater.inflate(R.layout.fragment_eventitem, container, false)

        mFirestore = FirebaseFirestore.getInstance()
        image_background = viewOfLayout.findViewById(R.id.image_eventItem)
        tv_title_event = viewOfLayout.findViewById(R.id.title_event)
        tv_localitzacio = viewOfLayout.findViewById(R.id.localitzacio)
        val documentID = requireActivity().intent.extras!!.getString("DOCUMENT_KEY")
        eventItem = mFirestore!!.collection("entryObject_DB").document(documentID!!)
        eventItem!!.get().addOnSuccessListener { documentSnapshot ->
            event = documentSnapshot.toObject(Event::class.java)
            Glide.with(image_background.getContext())
                    .load(event!!.URL)
                    .centerCrop()
                    .into(image_background)
            titol = event!!.Titol
            localitzacio = event!!.Localitzacio
            tv_title_event.setText(titol)
            tv_localitzacio.setText(localitzacio)
        }

        b_close = viewOfLayout.findViewById(R.id.close_activity)
        b_close.setOnClickListener(this)
        b_info = viewOfLayout.findViewById(R.id.b_info)
        b_comentari = viewOfLayout.findViewById(R.id.b_comentari)
        b_info.setOnClickListener(this)
        b_comentari.setOnClickListener(this)

        return viewOfLayout
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.close_activity -> requireActivity().onBackPressed()
            R.id.b_info -> {
                b_comentari.setBackgroundResource(R.drawable.btn_fragment_u)
                b_comentari.setTextColor(Color.BLACK)
                b_info.setBackgroundResource(R.drawable.btn_fragment_c)
                b_info.setTextColor(Color.WHITE)
                requireFragmentManager().beginTransaction().replace(R.id.fragment_event, ItemEvent_InfoFragment()).commit()
            }
            R.id.b_comentari -> {
                b_comentari.setBackgroundResource(R.drawable.btn_fragment_c)
                b_comentari.setTextColor(Color.WHITE)
                b_info.setBackgroundResource(R.drawable.btn_fragment_u)
                b_info.setTextColor(Color.BLACK)
                transaction = requireFragmentManager().beginTransaction()
                transaction!!.replace(R.id.fragment_event, ItemEvent_ComentFragment(), "")
                transaction!!.commit()
            }
        }
    }
}