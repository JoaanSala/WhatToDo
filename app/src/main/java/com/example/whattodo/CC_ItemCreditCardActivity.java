package com.example.whattodo;

import android.app.FragmentManager;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.whattodo.CCFragment.CCNameFragment;
import com.example.whattodo.CCFragment.CCNumberFragment;
import com.example.whattodo.CCFragment.CCSecureCodeFragment;
import com.example.whattodo.CCFragment.CCValidityFragment;
import com.example.whattodo.CCUtils.CreditCardUtils;
import com.example.whattodo.CCUtils.ViewPagerAdapter;
import com.example.whattodo.model.Coment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import org.w3c.dom.Document;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CC_ItemCreditCardActivity extends FragmentActivity implements FragmentManager.OnBackStackChangedListener {

    @BindView(R.id.btnNext)
    Button btnNext;

    public CC_CardFrontFragment cardFrontFragment;
    public CC_CardBackFragment cardBackFragment;

    //This is our viewPager
    private ViewPager viewPager;

    CCNumberFragment numberFragment;
    CCNameFragment nameFragment;
    CCValidityFragment validityFragment;
    CCSecureCodeFragment secureCodeFragment;

    int total_item;
    boolean backTrack = false;

    private boolean mShowingBack = false;

    String cardNumber, cardCVV, cardValidity, cardName;
    String userName;

    FirebaseAuth mAuth;
    FirebaseFirestore mStore;
    String userID;
    DocumentReference documentReference;
    EncryptDecrypt encryptDecrypt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_creditcard);

        ButterKnife.bind(this);
        encryptDecrypt = new EncryptDecrypt();
        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        btnNext = findViewById(R.id.btnNext);

        cardFrontFragment = new CC_CardFrontFragment();
        cardBackFragment = new CC_CardBackFragment();

        if (savedInstanceState == null) {
            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, cardFrontFragment).commit();

        } else {
            mShowingBack = (getFragmentManager().getBackStackEntryCount() > 0);
        }

        getFragmentManager().addOnBackStackChangedListener(this);

        //Initializing viewPager
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(4);
        setupViewPager(viewPager);

        documentReference = mStore.collection("users").document(userID);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                if(documentReference != null) {
                    userName = snapshot.getString("name")+' '+snapshot.getString("surname");
                }
            }
        });


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == total_item)
                    btnNext.setText("SUBMIT");
                else
                    btnNext.setText("NEXT");

                Log.d("track", "onPageSelected: " + position);

                if (position == total_item) {
                    flipCard();
                    backTrack = true;
                } else if (position == total_item - 1 && backTrack) {
                    flipCard();
                    backTrack = false;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = viewPager.getCurrentItem();
                Log.d("POS", String.valueOf(pos));
                if (pos < total_item) {
                    checkEntries(pos);
                } else {
                    checkEntries(3);
                }

            }
        });


    }

    public void checkEntries(int pos) {
        cardName = nameFragment.getName();
        cardNumber = numberFragment.getCardNumber();
        cardValidity = validityFragment.getValidity();
        cardCVV = secureCodeFragment.getValue();

        if (pos == 0) {
            if(TextUtils.isEmpty(cardNumber) || !CreditCardUtils.isValid(cardNumber.replace(" ",""))){
                Toast.makeText(CC_ItemCreditCardActivity.this, "Introdueix un numero de targeta correcte", Toast.LENGTH_SHORT).show();
            }else{
                viewPager.setCurrentItem(pos + 1);
            }

        } else if (pos == 1) {
            if(TextUtils.isEmpty(cardName)) {
                Toast.makeText(CC_ItemCreditCardActivity.this, "Aquest camp no pot estar buit", Toast.LENGTH_SHORT).show();
            }else{
                if(cardName.toLowerCase().equals(userName.toLowerCase())){
                    viewPager.setCurrentItem(pos + 1);
                }else{
                    Toast.makeText(CC_ItemCreditCardActivity.this, "El nom de la Targeta no correspon al del nostre usuari", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (pos == 2) {
            if(TextUtils.isEmpty(cardValidity) /*|| !CreditCardUtils.isValidDate(cardValidity)*/){
                Toast.makeText(CC_ItemCreditCardActivity.this, "Aquest camp no pot estar buit", Toast.LENGTH_SHORT).show();
            }else{
                if(CreditCardUtils.isValidDate(cardValidity)){
                    viewPager.setCurrentItem(pos + 1);
                }else{
                    Toast.makeText(CC_ItemCreditCardActivity.this, "Aquesta data està fora de validesa, o no es correcta", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (pos == 3) {
            if(TextUtils.isEmpty(cardCVV)||cardCVV.length()<3) {
                Toast.makeText(CC_ItemCreditCardActivity.this, "Introdueix un codi de seguretat correcte", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(CC_ItemCreditCardActivity.this, "La teva targeta s'ha afegit correctament", Toast.LENGTH_SHORT).show();
                addCreditCard();
                finish();
            }
        }
    }

    public void addCreditCard()
    {
        //encryptDecrypt.decryptingString(numberFragment.getCardNumber());

        String cardNumber = numberFragment.getCardNumber();
        String cardName = nameFragment.getName();
        String cardValidity = validityFragment.getValidity();
        String cardCVV = secureCodeFragment.getValue();

        //String documentID = encryptDecrypt.encryptingString(numberFragment.getCardNumber()+' '+nameFragment.getName());
        final DocumentReference CCDocument = mStore.collection("users").document(userID).collection("CreditCards").document();

        Map<String, Object> data = new HashMap<>();
        data.put("numTargeta", cardNumber);
        data.put("nameOwner", cardName);
        data.put("expiritDate", cardValidity);
        data.put("CVV", cardCVV);

        CCDocument.set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("TAG", "onSuccess : ofert Added with Id "+ CCDocument.getId());
            }
        });
    }

    @Override
    public void onBackStackChanged() {
        mShowingBack = (getFragmentManager().getBackStackEntryCount() > 0);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        numberFragment = new CCNumberFragment();
        nameFragment = new CCNameFragment();
        validityFragment = new CCValidityFragment();
        secureCodeFragment = new CCSecureCodeFragment();
        adapter.addFragment(numberFragment);
        adapter.addFragment(nameFragment);
        adapter.addFragment(validityFragment);
        adapter.addFragment(secureCodeFragment);

        total_item = adapter.getCount() - 1;
        viewPager.setAdapter(adapter);

    }

    private void flipCard() {
        if (mShowingBack) {
            getFragmentManager().popBackStack();
            return;
        }
        // Flip to the back.
        //setCustomAnimations(int enter, int exit, int popEnter, int popExit)

        mShowingBack = true;

        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        R.animator.card_flip_right_in,
                        R.animator.card_flip_right_out,
                        R.animator.card_flip_left_in,
                        R.animator.card_flip_left_out)
                .replace(R.id.fragment_container, cardBackFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onBackPressed() {
        int pos = viewPager.getCurrentItem();
        if (pos > 0) {
            viewPager.setCurrentItem(pos - 1);
        } else
            super.onBackPressed();
    }

    public void nextClick() {
        btnNext.performClick();
    }
}
