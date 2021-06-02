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
    private var mBirthDate: TextView? = null
    private var mDateSetListener: OnDateSetListener? = null
    private var b_backLogin: Button? = null
    private var b_crearCompte: Button? = null
    private var mName: EditText? = null
    private var mSurname: EditText? = null
    private var mCity: EditText? = null
    private var mEmail: EditText? = null
    private var mPwd: EditText? = null
    var mView: View? = null
    var mAuth: FirebaseAuth? = null
    var fStore: FirebaseFirestore? = null
    var userID: String? = null
    var userReference: DocumentReference? = null

    private lateinit var viewOfLayout: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewOfLayout = inflater.inflate(R.layout.fragment_signin, container, false)

        mName = viewOfLayout.findViewById(R.id.et_nom)
        mSurname = viewOfLayout.findViewById(R.id.et_cognoms)
        mCity = viewOfLayout.findViewById(R.id.et_ciutat)
        mEmail = viewOfLayout.findViewById(R.id.et_correu)
        mPwd = viewOfLayout.findViewById(R.id.et_contrasenya)
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
                        context!!,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year, month, day)
                dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog.show()
            }
            R.id.btn_backLogin -> fragmentManager!!.beginTransaction().replace(R.id.fragment_main,
                    LogInFragment()).commit()
            R.id.btn_crear_compte -> {
                val email = mEmail!!.text.toString().trim { it <= ' ' }
                val password = mPwd!!.text.toString().trim { it <= ' ' }
                val name = mName!!.text.toString().trim { it <= ' ' }
                val surname = mSurname!!.text.toString().trim { it <= ' ' }
                val city = mCity!!.text.toString().trim { it <= ' ' }
                val birthDate = mBirthDate!!.text.toString().trim { it <= ' ' }
                if (TextUtils.isEmpty(email)) {
                    mEmail!!.error = "Email is Required"
                } else if (TextUtils.isEmpty(password)) {
                    mPwd!!.error = "Password is required"
                } else if (password.length < 6) {
                    mPwd!!.error = "Password must ne >= 6 Characters"
                } else {
                    if (mAuth!!.currentUser != null) {
                        Toast.makeText(activity, "This Email is allready registered", Toast.LENGTH_SHORT).show()
                    } else {
                        mAuth!!.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(activity, "User Created.", Toast.LENGTH_SHORT).show()
                                userID = mAuth!!.currentUser.uid
                                userReference = fStore!!.collection("users").document(userID!!)
                                val user: MutableMap<String, Any> = HashMap()
                                user["name"] = name
                                user["surname"] = surname
                                user["email"] = email
                                user["city"] = city
                                user["birthDate"] = birthDate
                                user["currentLocation"] = "-"
                                userReference!!.set(user).addOnSuccessListener { Log.d("TAG", "onSuccess : user Profile is created for $userID") }
                                fragmentManager!!.beginTransaction().replace(R.id.fragment_main,
                                        LogInFragment()).commit()
                            } else {
                                Toast.makeText(context, "Error ! " + task.exception!!.message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }
    } /*private Task<Void> addOferts(final Ofert ofert) {

        final DocumentReference ofertRef = userReference.collection("userOferts").document();

        return fStore.runTransaction(new Transaction.Function<Void>() {
            @Nullable
            @Override
            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                    transaction.set(ofertRef, ofert);
                return null;
            }
        });

    }*/
}