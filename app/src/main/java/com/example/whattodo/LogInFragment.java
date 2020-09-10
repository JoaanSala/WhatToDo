package com.example.whattodo;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.whattodo.preferences.PreferencesActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.content.ContentValues.TAG;

public class LogInFragment extends Fragment implements View.OnClickListener{

    public EditText et_email, et_contrasenya;
    public TextView tv_forgotPwd;
    public String userID;
    View mView;
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    Button btn_logIn, btn_signIn;
    RelativeLayout loading_LogIn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_login, container, false);

        mAuth = FirebaseAuth.getInstance();

        btn_logIn = mView.findViewById(R.id.btn_logIn);
        loading_LogIn = mView.findViewById(R.id.loading_logIn);
        btn_signIn = mView.findViewById(R.id.btn_signIn);

        ImageView pref_login = mView.findViewById(R.id.pref_login);
        pref_login.setOnClickListener(this);

        tv_forgotPwd = mView.findViewById(R.id.forgotPwd);
        et_email = mView.findViewById(R.id.log_correo);
        et_contrasenya = mView.findViewById(R.id.log_contrasenya);

        btn_logIn.setOnClickListener(this);
        btn_signIn.setOnClickListener(this);
        tv_forgotPwd.setOnClickListener(this);

        return mView;
    }

    @Override
    public void onClick(View view) {
        Fragment selectedFragment;
        switch (view.getId()){
            case R.id.btn_logIn:
                //Iniciar Sessió
                String email = et_email.getText().toString().trim();
                String password = et_contrasenya.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    et_email.setError("Email is Required");
                }else if(TextUtils.isEmpty(password)){
                    et_contrasenya.setError("Password is required");
                }else {
                    btn_logIn.setVisibility(View.GONE);
                    loading_LogIn.setVisibility(View.VISIBLE);
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                //Comprovem si el compte està verificat
                                if (mAuth.getCurrentUser().isEmailVerified()) {
                                    Toast.makeText(getActivity(), "Logged in Succesfully", Toast.LENGTH_SHORT).show();
                                    getActivity().findViewById(R.id.bottom_navigation).setVisibility(View.VISIBLE);
                                    getFragmentManager().beginTransaction().replace(R.id.fragment_main,
                                            new HomeFragment()).commit();
                                } else {
                                    //Enviem email de verificació
                                    btn_logIn.setVisibility(View.VISIBLE);
                                    loading_LogIn.setVisibility(View.GONE);

                                    FirebaseUser fUser = mAuth.getCurrentUser();
                                    fUser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(getContext(), "Verification Email Has Been Sent", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d(TAG, "onFailure : Email not sent " + e.getMessage());
                                        }
                                    });
                                    Toast.makeText(getActivity(), "You have to verified your email first", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getActivity(), "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                btn_logIn.setVisibility(View.VISIBLE);
                                loading_LogIn.setVisibility(View.GONE);
                            }
                        }
                    });
                }
                break;

            case R.id.forgotPwd:
                //Canviar contrasenya
                final EditText resetMail = new EditText(getContext());
                AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(getContext());
                passwordResetDialog.setTitle("Resset Password ?");
                passwordResetDialog.setMessage("Enter Your Email To Received Reset Link");
                passwordResetDialog.setView(resetMail);

                passwordResetDialog.setPositiveButton("ACCEPT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //extract the email and send reset link
                        String mail = resetMail.getText().toString();
                        mAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getActivity(), "Reset Link Sent To Your Email", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(), "Error ! Reset Link is Not Sent" +e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                passwordResetDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //close the dialog
                    }
                });
                passwordResetDialog.create().show();
                break;

            case R.id.btn_signIn:
                selectedFragment = new SignInFragment();
                getFragmentManager().beginTransaction().replace(R.id.fragment_main,
                        selectedFragment).commit();
                break;

            case R.id.pref_login:
                startActivity(new Intent(getActivity(), PreferencesActivity.class));
                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        btn_logIn.setVisibility(View.VISIBLE);
        loading_LogIn.setVisibility(View.GONE);
    }
}
