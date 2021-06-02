package com.example.whattodo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.whattodo.R
import com.example.whattodo.adapter.ComentAdapter.ComentHolder
import com.example.whattodo.model.Coment
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class ComentAdapter(options: FirestoreRecyclerOptions<Coment?>) : FirestoreRecyclerAdapter<Coment, ComentHolder>(options) {
    class ComentHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var letterUser: TextView
        var mComent: TextView
        var mEstrella1: ImageView
        var mEstrella2: ImageView
        var mEstrella3: ImageView
        var mEstrella4: ImageView
        var mEstrella5: ImageView

        init {
            letterUser = itemView.findViewById(R.id.textUser)
            mComent = itemView.findViewById(R.id.textComentari)
            mEstrella1 = itemView.findViewById(R.id.com_estrella1)
            mEstrella2 = itemView.findViewById(R.id.com_estrella2)
            mEstrella3 = itemView.findViewById(R.id.com_estrella3)
            mEstrella4 = itemView.findViewById(R.id.com_estrella4)
            mEstrella5 = itemView.findViewById(R.id.com_estrella5)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComentHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_coment, parent, false)
        return ComentHolder(v)
    }

    override fun onBindViewHolder(comentHolder: ComentHolder, position: Int, coment: Coment) {
        comentHolder.letterUser.text = coment.userName
        comentHolder.mComent.text = coment.comentari
        val puntuacio = coment.puntuacio
        if (puntuacio >= 1) {
            comentHolder.mEstrella1.setImageResource(R.drawable.estrella_c)
        }
        if (puntuacio >= 2) {
            comentHolder.mEstrella2.setImageResource(R.drawable.estrella_c)
        }
        if (puntuacio >= 3) {
            comentHolder.mEstrella3.setImageResource(R.drawable.estrella_c)
        }
        if (puntuacio >= 4) {
            comentHolder.mEstrella4.setImageResource(R.drawable.estrella_c)
        }
        if (puntuacio == 5) {
            comentHolder.mEstrella5.setImageResource(R.drawable.estrella_c)
        }
    }
}