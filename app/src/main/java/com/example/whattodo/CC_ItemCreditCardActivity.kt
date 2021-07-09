package com.example.whattodo

import android.app.FragmentManager
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import butterknife.BindView
import butterknife.ButterKnife
import com.example.whattodo.CCFragment.CCNameFragment
import com.example.whattodo.CCFragment.CCNumberFragment
import com.example.whattodo.CCFragment.CCSecureCodeFragment
import com.example.whattodo.CCFragment.CCValidityFragment
import com.example.whattodo.CCUtils.CreditCardUtils.isValid
import com.example.whattodo.CCUtils.CreditCardUtils.isValidDate
import com.example.whattodo.CCUtils.ViewPagerAdapter
import com.google.android.gms.dynamic.SupportFragmentWrapper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

import androidx.fragment.app.FragmentManager.OnBackStackChangedListener

import java.util.*

class CC_ItemCreditCardActivity : FragmentActivity(),  OnBackStackChangedListener{
    @BindView(R.id.btnNext)
    lateinit var btnNext: Button
    lateinit var cardFrontFragment: CC_CardFrontFragment
    lateinit var cardBackFragment: CC_CardBackFragment

    //This is our viewPager
    lateinit var viewPager: ViewPager
    lateinit var numberFragment: CCNumberFragment
    lateinit var nameFragment: CCNameFragment
    lateinit var validityFragment: CCValidityFragment
    lateinit var secureCodeFragment: CCSecureCodeFragment

    lateinit var adapter: ViewPagerAdapter

    var total_item = 0
    var backTrack = false
    private var mShowingBack = false
    lateinit var cardNumber: String
    lateinit var cardCVV: String
    lateinit var cardValidity: String
    lateinit var cardName: String
    lateinit var userName: String
    lateinit var mAuth: FirebaseAuth
    lateinit var mStore: FirebaseFirestore
    lateinit var userID: String
    lateinit var documentReference: DocumentReference
    lateinit var encryptDecrypt: EncryptDecrypt

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_creditcard)

        ButterKnife.bind(this)
        encryptDecrypt = EncryptDecrypt()
        mAuth = FirebaseAuth.getInstance()
        mStore = FirebaseFirestore.getInstance()
        userID = mAuth.currentUser!!.uid
        btnNext = findViewById(R.id.btnNext)
        cardFrontFragment = CC_CardFrontFragment()
        cardBackFragment = CC_CardBackFragment()
        if (savedInstanceState == null) {
            // Add the fragment to the 'fragment_container' FrameLayout
            supportFragmentManager.beginTransaction()
                    .add(R.id.fragment_container, cardFrontFragment).commit()
        } else {
            mShowingBack = supportFragmentManager.backStackEntryCount > 0
        }
        supportFragmentManager.addOnBackStackChangedListener(this)

        //Initializing viewPager
        viewPager = findViewById(R.id.viewpager) as ViewPager
        viewPager.offscreenPageLimit = 4
        setupViewPager(viewPager)
        documentReference = mStore.collection("users").document(userID)
        documentReference.addSnapshotListener { snapshot, e ->
            if (documentReference != null) {
                userName = snapshot!!.getString("name") + ' ' + snapshot.getString("surname")
            }
        }
        viewPager.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {
                if (position == total_item) btnNext.setText("SUBMIT") else btnNext.setText("NEXT")
                Log.d("track", "onPageSelected: $position")
                if (position == total_item) {
                    flipCard()
                    backTrack = true
                } else if (position == total_item - 1 && backTrack) {
                    flipCard()
                    backTrack = false
                }
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
        btnNext.setOnClickListener(View.OnClickListener {
            val pos = viewPager.currentItem
            Log.d("POS", pos.toString())
            if (pos < total_item) {
                checkEntries(pos)
            } else {
                checkEntries(3)
            }
        })
    }

    fun checkEntries(pos: Int) {
        cardName = nameFragment.name.toString()
        cardNumber = numberFragment.cardNumber.toString()
        cardValidity = validityFragment.validity.toString()
        cardCVV = secureCodeFragment.value
        if (pos == 0) {
            if (TextUtils.isEmpty(cardNumber) || !isValid(cardNumber.replace(" ", ""))) {
                Toast.makeText(this@CC_ItemCreditCardActivity, "Introdueix un numero de targeta correcte", Toast.LENGTH_SHORT).show()
            } else {
                viewPager.currentItem = pos + 1
            }
        } else if (pos == 1) {
            if (TextUtils.isEmpty(cardName)) {
                Toast.makeText(this@CC_ItemCreditCardActivity, "Aquest camp no pot estar buit", Toast.LENGTH_SHORT).show()
            } else {
                if (cardName.toLowerCase() == userName.toLowerCase()) {
                    viewPager.currentItem = pos + 1
                } else {
                    Toast.makeText(this@CC_ItemCreditCardActivity, "El nom de la Targeta no correspon al del nostre usuari", Toast.LENGTH_SHORT).show()
                }
            }
        } else if (pos == 2) {
            if (TextUtils.isEmpty(cardValidity) /*|| !CreditCardUtils.isValidDate(cardValidity)*/) {
                Toast.makeText(this@CC_ItemCreditCardActivity, "Aquest camp no pot estar buit", Toast.LENGTH_SHORT).show()
            } else {
                if (isValidDate(cardValidity)) {
                    viewPager.currentItem = pos + 1
                } else {
                    Toast.makeText(this@CC_ItemCreditCardActivity, "Aquesta data està fora de validesa, o no es correcta", Toast.LENGTH_SHORT).show()
                }
            }
        } else if (pos == 3) {
            if (TextUtils.isEmpty(cardCVV) || cardCVV.length < 3) {
                Toast.makeText(this@CC_ItemCreditCardActivity, "Introdueix un codi de seguretat correcte", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@CC_ItemCreditCardActivity, "La teva targeta s'ha afegit correctament", Toast.LENGTH_SHORT).show()
                addCreditCard()
                finish()
            }
        }
    }

    fun addCreditCard() {
        //encryptDecrypt.decryptingString(numberFragment.getCardNumber());
        val cardNumber = numberFragment.cardNumber
        val cardName = nameFragment.name
        val cardValidity = validityFragment.validity
        val cardCVV = secureCodeFragment.value

        //String documentID = encryptDecrypt.encryptingString(numberFragment.getCardNumber()+' '+nameFragment.getName());
        val CCDocument = mStore!!.collection("users").document(userID!!).collection("CreditCards").document()
        val data: MutableMap<String, Any?> = HashMap()
        data["numTargeta"] = cardNumber
        data["nameOwner"] = cardName
        data["expiritDate"] = cardValidity
        data["CVV"] = cardCVV
        CCDocument.set(data).addOnSuccessListener { Log.d("TAG", "onSuccess : ofert Added with Id " + CCDocument.id) }
    }

    override fun onBackStackChanged() {
        mShowingBack = supportFragmentManager.backStackEntryCount > 0
    }

    private fun setupViewPager(viewPager: ViewPager) {
        adapter = ViewPagerAdapter(supportFragmentManager)

        numberFragment = CCNumberFragment()
        nameFragment = CCNameFragment()
        validityFragment = CCValidityFragment()
        secureCodeFragment = CCSecureCodeFragment()

        adapter.addFragment(numberFragment)
        adapter.addFragment(nameFragment)
        adapter.addFragment(validityFragment)
        adapter.addFragment(secureCodeFragment)

        total_item = adapter.count - 1

        viewPager.adapter = adapter
    }

    private fun flipCard() {
        if (mShowingBack) {
            supportFragmentManager.popBackStack()
            return
        }
        // Flip to the back.
        //setCustomAnimations(int enter, int exit, int popEnter, int popExit)
        mShowingBack = true
        supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(
                        R.animator.card_flip_right_in,
                        R.animator.card_flip_right_out,
                        R.animator.card_flip_left_in,
                        R.animator.card_flip_left_out)
                .replace(R.id.fragment_container, cardBackFragment)
                .addToBackStack(null)
                .commit()
    }

    override fun onBackPressed() {
        val pos = viewPager.currentItem
        if (pos > 0) {
            viewPager.currentItem = pos - 1
        } else super.onBackPressed()
    }

    fun nextClick() {
        btnNext.performClick()
    }
}

