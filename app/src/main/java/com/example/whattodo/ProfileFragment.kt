package com.example.whattodo

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.whattodo.preferences.PreferencesActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class ProfileFragment : Fragment(), View.OnClickListener {
    var rv_email: RelativeLayout? = null
    var rv_ciutat: RelativeLayout? = null
    var rv_date: RelativeLayout? = null
    var profileImage: ImageView? = null
    var name: TextView? = null
    var email: TextView? = null
    var city: TextView? = null
    var date: TextView? = null
    var profileText: TextView? = null
    var mAuth: FirebaseAuth? = null
    var mStore: FirebaseFirestore? = null
    var userID: String? = null
    var documentReference: DocumentReference? = null

    private lateinit var viewOfLayout: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewOfLayout = inflater.inflate(R.layout.fragment_profile, container, false)

        profileImage = viewOfLayout.findViewById(R.id.profile_image)
        profileText = viewOfLayout.findViewById(R.id.profile_text)
        name = viewOfLayout.findViewById(R.id.text_profile)
        email = viewOfLayout.findViewById(R.id.email_c)
        city = viewOfLayout.findViewById(R.id.ciutat_c)
        date = viewOfLayout.findViewById(R.id.birth_c)
        rv_email = viewOfLayout.findViewById(R.id.email_rl)
        rv_ciutat = viewOfLayout.findViewById(R.id.ciutat_rl)
        rv_date = viewOfLayout.findViewById(R.id.birth_rl)

        mAuth = FirebaseAuth.getInstance()
        mStore = FirebaseFirestore.getInstance()
        userID = mAuth!!.currentUser.uid

        val logout = viewOfLayout.findViewById<Button>(R.id.butt_logout)
        logout.setOnClickListener(this)

        setProfile()

        val pref_profile = viewOfLayout.findViewById<ImageView>(R.id.pref_profile)
        pref_profile.setOnClickListener(this)

        return viewOfLayout
    }

    fun setProfile() {
        documentReference = mStore!!.collection("users").document(userID!!)
        documentReference!!.addSnapshotListener { snapshot, e ->
            if (documentReference != null) {
                val userName = snapshot!!.getString("name")
                val userSurname = snapshot.getString("surname")
                name!!.text = "$userName $userSurname".toUpperCase()
                email!!.text = snapshot.getString("email")
                city!!.text = snapshot.getString("city")
                date!!.text = snapshot.getString("birthDate")
                val firstLetter = Character.toString(userName!![0]) + Character.toString(userSurname!![0])
                profileText!!.text = firstLetter
            }
        }
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.pref_profile -> startActivity(Intent(activity, PreferencesActivity::class.java))
            R.id.butt_logout -> {
                mAuth = FirebaseAuth.getInstance()
                mAuth!!.signOut()
                startActivity(Intent(activity, MainActivity::class.java))
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(viewOfLayout!!.context)
        if (mySharedPreferences.getBoolean("enableEmail", true)) {
            rv_email!!.visibility = View.VISIBLE
        } else {
            rv_email!!.visibility = View.GONE
        }
        if (mySharedPreferences.getBoolean("enableDate", true)) {
            rv_date!!.visibility = View.VISIBLE
        } else {
            rv_date!!.visibility = View.GONE
        }
        if (mySharedPreferences.getBoolean("enableCiutat", true)) {
            rv_ciutat!!.visibility = View.VISIBLE
        } else {
            rv_ciutat!!.visibility = View.GONE
        }
    }

    override fun onPause() {
        super.onPause()
        documentReference = null
    }

    override fun onStop() {
        super.onStop()
        documentReference = null
    }
}