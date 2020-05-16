package com.example.whattodo;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;

import static android.content.ContentValues.TAG;

public class SignInFragment extends Fragment implements View.OnClickListener {

    private TextView mBirthDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private Button b_backLogin, b_crearCompte;
    private EditText mName, mSurname, mCity, mEmail, mPwd;

    View mView;
    FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_signin, container, false);

        mName = mView.findViewById(R.id.et_nom);
        mSurname = mView.findViewById(R.id.et_cognoms);
        mCity = mView.findViewById(R.id.et_ciutat);
        mEmail = mView.findViewById(R.id.et_correu);
        mPwd = mView.findViewById(R.id.et_contrasenya);
        mBirthDate = mView.findViewById(R.id.et_dataNaixement);

        mAuth = FirebaseAuth.getInstance();

        b_backLogin = mView.findViewById(R.id.btn_backLogin);
        b_crearCompte = mView.findViewById(R.id.btn_crear_compte);

        mBirthDate.setOnClickListener(this);
        b_backLogin.setOnClickListener(this);
        b_crearCompte.setOnClickListener(this);


        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = day + "/" + month + "/" + year;
                mBirthDate.setText(date);
            }
        };

        return mView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.et_dataNaixement:
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        getContext(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
                break;
            case R.id.btn_backLogin:
                getFragmentManager().beginTransaction().replace(R.id.fragment_main,
                        new LogInFragment()).commit();
                break;
            case R.id.btn_crear_compte:
                String email = mEmail.getText().toString().trim();
                String password = mPwd.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Email is Required");
                }else if(TextUtils.isEmpty(password)){
                    mPwd.setError("Password is required");
                }else if(password.length() < 6){
                    mPwd.setError("Password must ne >= 6 Characters");
                }

                if(mAuth.getCurrentUser() != null){
                    Toast.makeText(getActivity(), "This Email is allready registered", Toast.LENGTH_SHORT).show();
                }else {
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getActivity(), "User Created.", Toast.LENGTH_SHORT).show();
                                getFragmentManager().beginTransaction().replace(R.id.fragment_main,
                                        new LogInFragment()).commit();
                            } else {
                                Toast.makeText(getContext(), "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
        }
    }
}
