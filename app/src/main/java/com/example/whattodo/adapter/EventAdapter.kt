package com.example.whattodo.adapter

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.whattodo.R
import com.example.whattodo.adapter.EventAdapter.EventHolder
import com.example.whattodo.model.Event
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.android.synthetic.main.item_event.view.*

class EventAdapter(private val options: FirestoreRecyclerOptions<Event>, private val listener: OnItemClickListener) : FirestoreRecyclerAdapter<Event, EventHolder>(options) {

    interface OnItemClickListener {
        fun onItemClick(documentSnapshot: DocumentSnapshot?, position: Int)
    }

    inner class EventHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var mImageView: ImageView = itemView.imageView
        var mTextView1: TextView = itemView.textCardNumber
        var mTextView2: TextView = itemView.textValidateDate
        var mTextView3: TextView = itemView.textView3
        var mTextView4: TextView = itemView.textView4

        fun bind(event: Event) {
            Glide.with(itemView.context)
                    .load(event.URL)
                    .into(mImageView)
        }

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(snapshots.getSnapshot(position), position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
        return EventHolder(v)
    }

    override fun onBindViewHolder(eventHolder: EventHolder, position: Int, event: Event) {
        eventHolder.bind(event)
        eventHolder.mTextView1.text = event.Titol
        eventHolder.mTextView2.text = event.Adreça
        eventHolder.mTextView3.text = event.Localitzacio
        eventHolder.mTextView4.text = event.Qualificacio
        val color = event.Qualificacio!!.toDouble()
        if (color >= 8.5) {
            eventHolder.mTextView4.setTextColor(Color.parseColor("#4CAF50"))
        } //green
        else if (color < 8.5 && color >= 7) {
            eventHolder.mTextView4.setTextColor(Color.parseColor("#17B3FA"))
        } //blue
        else if (color < 7 && color >= 6) {
            eventHolder.mTextView4.setTextColor(Color.parseColor("#FFCE1C"))
        } //yellow
        else if (color < 6 && color >= 5) {
            eventHolder.mTextView4.setTextColor(Color.parseColor("#FF8F19"))
        } //orange
        else if (color < 5) {
            eventHolder.mTextView4.setTextColor(Color.parseColor("#F13123"))
        } //red
    }
}