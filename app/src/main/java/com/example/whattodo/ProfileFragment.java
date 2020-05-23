package com.example.whattodo;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.whattodo.preferences.PreferencesActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment implements View.OnClickListener{

    View mView;
    RelativeLayout rv_email, rv_ciutat, rv_date;
    ImageView pref_profile;
    ImageView profileImage;
    TextView name, email, city, date, profileText;

    FirebaseAuth mAuth;
    FirebaseFirestore mStore;
    String userID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_profile, container, false);

        profileImage = mView.findViewById(R.id.profile_image);
        profileText = mView.findViewById(R.id.profile_text);
        name = mView.findViewById(R.id.text_profile);
        email = mView.findViewById(R.id.email_c);
        city = mView.findViewById(R.id.ciutat_c);
        date = mView.findViewById(R.id.birth_c);

        rv_email = mView.findViewById(R.id.email_rl);
        rv_ciutat = mView.findViewById(R.id.ciutat_rl);
        rv_date = mView.findViewById(R.id.birth_rl);

        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();
        userID = mAuth.getCurrentUser().getUid();

        setProfile();

        pref_profile = mView.findViewById(R.id.pref_profile);
        pref_profile.setOnClickListener(this);

        return mView;
    }

    public void setProfile(){
        DocumentReference documentReference = mStore.collection("users").document(userID);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
            String userName = snapshot.getString("name");
            String userSurname = snapshot.getString("surname");
            name.setText((userName+" "+userSurname).toUpperCase());
            email.setText(snapshot.getString("email"));
            city.setText(snapshot.getString("city"));
            date.setText(snapshot.getString("birthDate"));

            String firstLetter = Character.toString(userName.charAt(0))+Character.toString(userSurname.charAt(0));
            profileText.setText(firstLetter);
            }
        });

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


        SharedPreferences mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(mView.getContext());

        if(mySharedPreferences.getBoolean("enableEmail", true)){
            rv_email.setVisibility(View.VISIBLE);
        }else{
            rv_email.setVisibility(View.GONE);
        }

        if(mySharedPreferences.getBoolean("enableDate", true)){
            rv_date.setVisibility(View.VISIBLE);
        }else{
            rv_date.setVisibility(View.GONE);
        }

        if(mySharedPreferences.getBoolean("enableCiutat", true)){
            rv_ciutat.setVisibility(View.VISIBLE);
        }else{
            rv_ciutat.setVisibility(View.GONE);
        }


    }
}
