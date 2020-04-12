package com.example.whattodo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.whattodo.preferences.PreferencesActivity;

public class ProfileFragment extends Fragment implements View.OnClickListener{

    View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_profile, container, false);

        ImageView pref_profile = mView.findViewById(R.id.pref_profile);
        pref_profile.setOnClickListener(this);

        return mView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pref_profile:
                startActivity(new Intent(getActivity(), PreferencesActivity.class));
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        RelativeLayout email = mView.findViewById(R.id.email_rl);
        RelativeLayout ciutat = mView.findViewById(R.id.ciutat_rl);
        RelativeLayout date = mView.findViewById(R.id.birth_rl);

        SharedPreferences mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(mView.getContext());

        if(mySharedPreferences.getBoolean("enableEmail", true)){
            email.setVisibility(View.VISIBLE);
        }else{
            email.setVisibility(View.GONE);
        }

        if(mySharedPreferences.getBoolean("enableDate", true)){
            date.setVisibility(View.VISIBLE);
        }else{
            date.setVisibility(View.GONE);
        }

        if(mySharedPreferences.getBoolean("enableCiutat", true)){
            ciutat.setVisibility(View.VISIBLE);
        }else{
            ciutat.setVisibility(View.GONE);
        }


    }
}
