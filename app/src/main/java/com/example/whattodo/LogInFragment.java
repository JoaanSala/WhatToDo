package com.example.whattodo;

import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.whattodo.preferences.PreferencesActivity;

public class LogInFragment extends Fragment implements View.OnClickListener{

    public EditText et_email, et_contrasenya;
    public String email, contrasenya;
    View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_login, container, false);

        Button btn_logIn = mView.findViewById(R.id.iniciar_sessio);
        Button btn_signIn = mView.findViewById(R.id.btn_ir_crearCuenta);

        ImageView pref_login = mView.findViewById(R.id.pref_login);
        pref_login.setOnClickListener(this);

        et_email = mView.findViewById(R.id.log_correo);
        et_contrasenya = mView.findViewById(R.id.log_contrasenya);
        email = et_email.getText().toString();
        contrasenya = et_contrasenya.getText().toString();


        btn_logIn.setOnClickListener(this);
        btn_signIn.setOnClickListener(this);

        return mView;
    }

    @Override
    public void onClick(View view) {
        Fragment selectedFragment;
        switch (view.getId()){
            case R.id.iniciar_sessio:
                SharedPreferences mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(mView.getContext());
                if(mySharedPreferences.getBoolean("enableSuperuser", false)){
                    getActivity().findViewById(R.id.bottom_navigation).setVisibility(View.VISIBLE);
                    selectedFragment = new HomeFragment();
                    getFragmentManager().beginTransaction().replace(R.id.fragment_main,
                            selectedFragment).commit();
                }else{
                    Toast.makeText(getContext(), "Activa el mode superUsuari del menú, per poder accedir a l'aplicació",Toast.LENGTH_LONG).show();
                }


                break;
            case R.id.btn_ir_crearCuenta:
                selectedFragment = new SignInFragment();
                getFragmentManager().beginTransaction().replace(R.id.fragment_main,
                        selectedFragment).commit();
                break;

            case R.id.pref_login:
                startActivity(new Intent(getActivity(), PreferencesActivity.class));
                break;
        }
    }

}
