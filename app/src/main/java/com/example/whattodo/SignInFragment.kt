package com.example.whattodo

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_signin.*
import java.util.*

class SignInFragment : Fragment(), View.OnClickListener {
    private lateinit var mBirthDate: TextView

    private var mDateSetListener: OnDateSetListener? = null

    private lateinit var b_backLogin: Button
    private lateinit var b_crearCompte: Button

    private lateinit var mName: EditText
    private lateinit var mSurname: EditText
    private lateinit var mCity: EditText
    private lateinit var mEmail: EditText
    private lateinit var mPwd: EditText

    private lateinit var viewOfLayout: View
    private lateinit var mAuth: FirebaseAuth
    private lateinit var fStore: FirebaseFirestore
    private lateinit var userID: String
    private lateinit var userReference: DocumentReference

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        viewOfLayout = inflater.inflate(R.layout.fragment_signin, container, false)

        mName = viewOfLayout.findViewById(R.id.et_nom)
        mSurname = viewOfLayout.findViewById(R.id.et_cognoms)
        mCity = viewOfLayout.findViewById(R.id.et_ciutat)
        mEmail = viewOfLayout.findViewById(R.id.et_correu)
        mPwd = viewOfLayout.findViewById(R.id.et_contrasenya)
        mBirthDate = viewOfLayout.findViewById(R.id.tv_dataNaixement)

        mAuth = FirebaseAuth.getInstance()

        fStore = FirebaseFirestore.getInstance()

        val tv_dataNaixement = viewOfLayout.findViewById<TextView>(R.id.tv_dataNaixement)
        val btn_backLogin = viewOfLayout.findViewById<Button>(R.id.btn_backLogin)
        val btn_crear_compte = viewOfLayout.findViewById<Button>(R.id.btn_crear_compte)

        tv_dataNaixement.setOnClickListener(this)
        btn_backLogin.setOnClickListener(this)
        btn_crear_compte.setOnClickListener(this)

        mDateSetListener = OnDateSetListener { datePicker, year, month, day ->
            var month = month
            month = month + 1
            val date = "$day/$month/$year"
            tv_dataNaixement.setText(date)
        }
        return viewOfLayout
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.tv_dataNaixement -> {
                val cal = Calendar.getInstance()
                val year = cal[Calendar.YEAR]
                val month = cal[Calendar.MONTH]
                val day = cal[Calendar.DAY_OF_MONTH]
                val dialog = DatePickerDialog(
                    requireContext(),
                    android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                    mDateSetListener,
                    year, month, day
                )
                dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog.show()
            }
            R.id.btn_backLogin -> requireFragmentManager().beginTransaction().replace(
                R.id.fragment_main,
                LogInFragment()
            ).commit()
            R.id.btn_crear_compte -> {
                val email = mEmail.text.toString().trim { it <= ' ' }
                val password = mPwd.text.toString().trim { it <= ' ' }
                val name = mName.text.toString().trim { it <= ' ' }
                val surname = mSurname.text.toString().trim { it <= ' ' }
                val city = mCity.text.toString().trim { it <= ' ' }
                val birthDate = mBirthDate.text.toString().trim { it <= ' ' }

                if (TextUtils.isEmpty(email)) {
                    mEmail.error = "EMAIL NECESSARI"
                } else if (TextUtils.isEmpty(password)) {
                    mPwd.error = "FA FALTA CONTRASSENYA"
                } else if (password.length < 6) {
                    mPwd.error = "CONTRASENYA MASSA CURTA... MÍNIM 6 CARÀCTERS!"
                } else {
                    if (mAuth.currentUser == null) {
                        Log.d("CurrentUsers: ", mAuth.currentUser!!.displayName!!)
                        Toast.makeText(
                            activity,
                            "AQUEST EMAIL JA ESTA REGISTRAT",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        mAuth!!.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(activity, "USUARI CREAT!.", Toast.LENGTH_SHORT)
                                        .show()
                                    userID = mAuth!!.currentUser!!.uid
                                    userReference = fStore!!.collection("users").document(userID!!)
                                    val user: MutableMap<String, Any> = HashMap()
                                    user["name"] = name
                                    user["surname"] = surname
                                    user["email"] = email
                                    user["city"] = city
                                    user["birthDate"] = birthDate
                                    user["currentLocation"] = "-"
                                    userReference!!.set(user).addOnSuccessListener {
                                        Log.d(
                                            "TAG",
                                            "onSuccess : user Profile is created for $userID"
                                        )
                                    }
                                    requireFragmentManager().beginTransaction().replace(
                                        R.id.fragment_main,
                                        LogInFragment()
                                    ).commit()
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Error ! " + task.exception!!.message,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                    }
                }
            }
        }
    }
}

