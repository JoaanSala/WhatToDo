package com.example.whattodo.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.whattodo.R
import com.example.whattodo.adapter.OfertAdapter.OfertHolder
import com.example.whattodo.model.Ofert
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.item_ofert.view.*
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter

class OfertAdapter(private val options: FirestoreRecyclerOptions<Ofert>, private val listener: OnItemClickListener) : FirestoreRecyclerAdapter<Ofert, OfertHolder>(options) {

    interface OnItemClickListener {
        fun onItemClick(documentSnapshot: DocumentSnapshot?, position: Int)
    }

    inner class OfertHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var bt_getOfert = itemView.button_adquirir
        var eventANDlocation = itemView.event_and_location
        var title = itemView.ofert_title
        var caducitat = itemView.text_caducitat
        var duration = itemView.text_duration
        var ofertaAdquired = itemView.oferta_adquirida

        var imageOfert = itemView.image_ofert

        fun bind(ofert: Ofert, position: Int) {
            Glide.with(imageOfert.context)
                    .load(ofert.photo)
                    .into(imageOfert)

            val userID = FirebaseAuth.getInstance().currentUser.uid
            val ofertDocument = FirebaseFirestore.getInstance().collection("users").document(userID).collection("PaidOferts").document(snapshots.getSnapshot(position).id)
            ofertDocument.addSnapshotListener { snapshot, e ->
                if (snapshot!!.getBoolean("acquired") != null) {
                    if (snapshot.getBoolean("acquired") == true) {
                        bt_getOfert.visibility = View.GONE
                        ofertaAdquired.visibility = View.VISIBLE
                    } else {
                        bt_getOfert.visibility = View.VISIBLE
                        ofertaAdquired.visibility = View.GONE
                        Log.d("OFERTID", "This ofert isn't adquire!")
                    }
                }
            }
        }

        init {
            bt_getOfert.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(snapshots.getSnapshot(position), position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OfertHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_ofert, parent, false)
        return OfertHolder(v)
    }

    override fun onBindViewHolder(ofertHolder: OfertHolder, i: Int, ofert: Ofert) {
        ofertHolder.bind(ofert, i)
        ofertHolder.eventANDlocation.text = ofert.event + ", " + ofert.localitzacio
        ofertHolder.title.text = ofert.title
        ofertHolder.caducitat.text = "Oferta Vàlida fins " + ofert.validesa
        if (ofert.price == "0,00") {
            ofertHolder.bt_getOfert.text = "ADQUIRIR GRATIS"
        } else {
            ofertHolder.bt_getOfert.text = "€ " + ofert.price
            ofertHolder.bt_getOfert.textSize = 25f
        }
        val getDate = ofert.validesa + "/2020"
        val sdf = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val futureDate = LocalDate.parse(getDate, sdf)
        val presentDate = LocalDate.now()
        val period = Period.between(presentDate, futureDate)
        ofertHolder.duration.text = "Finalitza en:  " + period.months + " Mesos -" + period.days + " Dies"
    }
}