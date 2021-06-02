package com.example.whattodo

import android.app.AlertDialog
import android.content.ContentValues
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.fragment_login.*

class LogInFragment : Fragment(), View.OnClickListener {
    var et_email: EditText? = null
    var et_contrasenya: EditText? = null
    var userID: String? = null
    private var mAuth: FirebaseAuth? = null
    private val user: FirebaseUser? = null
    var loading_LogIn: RelativeLayout? = null

    private lateinit var viewOfLayout: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewOfLayout = inflater!!.inflate(R.layout.fragment_login, container, false)

        mAuth = FirebaseAuth.getInstance()
        loading_LogIn = viewOfLayout.findViewById(R.id.loading_logIn)
        et_email = viewOfLayout.findViewById(R.id.log_correo)
        et_contrasenya = viewOfLayout.findViewById(R.id.log_contrasenya)

        val btn_logIn = viewOfLayout.findViewById<Button>(R.id.btn_logIn)
        val btn_signIn = viewOfLayout.findViewById<Button>(R.id.btn_signIn)
        val forgotPwd = viewOfLayout.findViewById<TextView>(R.id.forgotPwd)

        btn_logIn.setOnClickListener(this)
        btn_signIn.setOnClickListener(this)
        forgotPwd.setOnClickListener(this)

        return viewOfLayout
    }

    override fun onClick(view: View) {
        val selectedFragment: Fragment
        when (view.id) {
            R.id.btn_logIn -> {
                //Iniciar Sessió
                val email = et_email!!.text.toString().trim { it <= ' ' }
                val password = et_contrasenya!!.text.toString().trim { it <= ' ' }
                if (TextUtils.isEmpty(email)) {
                    et_email!!.error = "Email is Required"
                } else if (TextUtils.isEmpty(password)) {
                    et_contrasenya!!.error = "Password is required"
                } else {
                    btn_logIn!!.visibility = View.GONE
                    loading_LogIn!!.visibility = View.VISIBLE
                    mAuth!!.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            //Comprovem si el compte està verificat
                            if (mAuth!!.currentUser.isEmailVerified) {
                                Toast.makeText(activity, "Logged in Succesfully", Toast.LENGTH_SHORT).show()
                                activity!!.findViewById<View>(R.id.bottom_navigation).visibility = View.VISIBLE
                                fragmentManager!!.beginTransaction().replace(R.id.fragment_main,
                                        HomeFragment()).commit()
                            } else {
                                //Enviem email de verificació
                                btn_logIn!!.visibility = View.VISIBLE
                                loading_LogIn!!.visibility = View.GONE
                                val fUser = mAuth!!.currentUser
                                fUser.sendEmailVerification().addOnSuccessListener { Toast.makeText(context, "Verification Email Has Been Sent", Toast.LENGTH_SHORT).show() }.addOnFailureListener { e -> Log.d(ContentValues.TAG, "onFailure : Email not sent " + e.message) }
                                Toast.makeText(activity, "You have to verified your email first", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(activity, "Error ! " + task.exception!!.message, Toast.LENGTH_SHORT).show()
                            btn_logIn!!.visibility = View.VISIBLE
                            loading_LogIn!!.visibility = View.GONE
                        }
                    }
                }
            }
            R.id.forgotPwd -> {
                //Canviar contrasenya
                val resetMail = EditText(context)
                val passwordResetDialog = AlertDialog.Builder(context)
                passwordResetDialog.setTitle("Resset Password ?")
                passwordResetDialog.setMessage("Enter Your Email To Received Reset Link")
                passwordResetDialog.setView(resetMail)
                passwordResetDialog.setPositiveButton("ACCEPT") { dialogInterface, i -> //extract the email and send reset link
                    val mail = resetMail.text.toString()
                    mAuth!!.sendPasswordResetEmail(mail).addOnSuccessListener { Toast.makeText(activity, "Reset Link Sent To Your Email", Toast.LENGTH_SHORT).show() }.addOnFailureListener { e -> Toast.makeText(activity, "Error ! Reset Link is Not Sent" + e.message, Toast.LENGTH_SHORT).show() }
                }
                passwordResetDialog.setNegativeButton("CANCEL") { dialogInterface, i ->
                    //close the dialog
                }
                passwordResetDialog.create().show()
            }
            R.id.btn_signIn -> {
                selectedFragment = SignInFragment()
                fragmentManager!!.beginTransaction().replace(R.id.fragment_main,
                        selectedFragment).commit()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        btn_logIn!!.visibility = View.VISIBLE
        loading_LogIn!!.visibility = View.GONE
    }
}