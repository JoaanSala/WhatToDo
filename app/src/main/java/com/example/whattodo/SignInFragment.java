package com.example.whattodo;

import androidx.fragment.app.Fragment;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

public class SignInFragment extends Fragment implements View.OnClickListener {

    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private Button b_backLogin, b_crearCompte;

    View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_signin, container, false);

        mDisplayDate = mView.findViewById(R.id.et_dataNaixement);
        b_backLogin = mView.findViewById(R.id.btn_backLogin);
        b_crearCompte = mView.findViewById(R.id.btn_crear_compte);

        mDisplayDate.setOnClickListener(this);
        b_backLogin.setOnClickListener(this);
        b_crearCompte.setOnClickListener(this);


        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                String date = day + "/" + month + "/" + year;
                mDisplayDate.setText(date);
            }
        };

        return mView;
    }

    @Override
    public void onClick(View view) {
        Fragment selectedFragment = null;
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
                selectedFragment = new LogInFragment();
                getFragmentManager().beginTransaction().replace(R.id.fragment_main,
                        selectedFragment).commit();
                break;
            case R.id.btn_crear_compte:
                selectedFragment = new LogInFragment();
                getFragmentManager().beginTransaction().replace(R.id.fragment_main,
                        selectedFragment).commit();
                break;
        }
    }
}
