package com.example.whattodo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import com.example.whattodo.model.Ofert;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.database.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class SignInFragment extends Fragment implements View.OnClickListener {

    private TextView mBirthDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private Button b_backLogin, b_crearCompte;
    private EditText mName, mSurname, mCity, mEmail, mPwd;

    View mView;
    FirebaseAuth mAuth;
    FirebaseFirestore fStore;
    String userID;
    DocumentReference userReference;

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
        fStore = FirebaseFirestore.getInstance();

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
                final String email = mEmail.getText().toString().trim();
                String password = mPwd.getText().toString().trim();
                final String name = mName.getText().toString().trim();
                final String surname = mSurname.getText().toString().trim();
                final String city = mCity.getText().toString().trim();
                final String birthDate = mBirthDate.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Email is Required");
                }else if(TextUtils.isEmpty(password)){
                    mPwd.setError("Password is required");
                }else if(password.length() < 6){
                    mPwd.setError("Password must ne >= 6 Characters");
                }else {
                    if (mAuth.getCurrentUser() != null) {
                        Toast.makeText(getActivity(), "This Email is allready registered", Toast.LENGTH_SHORT).show();
                    } else {
                        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getActivity(), "User Created.", Toast.LENGTH_SHORT).show();
                                    userID = mAuth.getCurrentUser().getUid();
                                    userReference = fStore.collection("users").document(userID);
                                    Map<String, Object> user = new HashMap<>();
                                    user.put("name", name);
                                    user.put("surname", surname);
                                    user.put("email", email);
                                    user.put("city", city);
                                    user.put("birthDate", birthDate);
                                    userReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d("TAG", "onSuccess : user Profile is created for "+userID);
                                        }
                                    });
                                    fStore.collection("PaidOferts")
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    /*if(task.isSuccessful()){
                                                        int count = 0;
                                                        for(QueryDocumentSnapshot document : task.getResult()){
                                                            Log.d("Copyin documents", document.getId() + " => " + document.getData());
                                                            addOferts(document.toObject(Ofert.class));
                                                            count++;
                                                        }
                                                        Log.d("How many Oferts", String.valueOf(count));

                                                    }*/
                                                }
                                            });


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


    /*private Task<Void> addOferts(final Ofert ofert) {

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
