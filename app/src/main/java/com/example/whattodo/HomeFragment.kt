package com.example.whattodo

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.messaging.FirebaseMessaging
import java.io.IOException
import java.text.DateFormat
import java.text.Normalizer
import java.util.*

class HomeFragment : Fragment(), View.OnClickListener {

    private lateinit var mLatitudeLabel: String
    private lateinit var mLongitudeLabel: String
    private lateinit var mLocationRequest: LocationRequest
    private lateinit var mLocationSettingsRequest: LocationSettingsRequest

    private var mRequestingLocationUpdates: Boolean? = null
    private lateinit var mSettingsClient: SettingsClient
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private lateinit var mLocationCallback: LocationCallback
    private lateinit var mCurrentLocation: Location
    private val mLastUpdateTimeLabel: String? = null
    private lateinit var mLastUpdateTime: String

    private lateinit var getLocation: Button
    private lateinit var restaurants: CardView
    private lateinit var monuments: CardView
    private lateinit var oci: CardView
    private lateinit var nocturn: CardView

    private lateinit var viewOfLayout: View

    private lateinit var city_location: TextView
    var latitude = 0.0
    var longitude = 0.0

    private lateinit var ma: MainActivity
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mStore: FirebaseFirestore

    private lateinit var userID: String
    private lateinit var documentReference: DocumentReference

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle? ): View? {
        viewOfLayout = inflater.inflate(R.layout.fragment_home, container, false)
        ma = (activity as MainActivity)
        mAuth = FirebaseAuth.getInstance()
        mStore = FirebaseFirestore.getInstance()
        userID = mAuth!!.currentUser!!.uid
        initLocationUser()

        restaurants = viewOfLayout.findViewById(R.id.cardivew_r)
        monuments = viewOfLayout.findViewById(R.id.cardivew_m)
        oci = viewOfLayout.findViewById(R.id.cardivew_o)
        nocturn = viewOfLayout.findViewById(R.id.cardivew_n)

        restaurants.setOnClickListener(this)
        monuments.setOnClickListener(this)
        oci.setOnClickListener(this)
        nocturn.setOnClickListener(this)

        city_location = viewOfLayout.findViewById(R.id.c_location_nearby)
        mLatitudeLabel = resources.getString(R.string.latitude_label)
        mLongitudeLabel = resources.getString(R.string.longitude_label)

        getLocation = viewOfLayout.findViewById(R.id.search_location)
        getLocation.setOnClickListener(this)

        mRequestingLocationUpdates = false
        mLastUpdateTime = ""
        updateValuesFromBundle(savedInstanceState)

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        mSettingsClient = LocationServices.getSettingsClient(requireActivity())

        createLocationCallback()
        createLocationRequest()
        buildLocationSettingsRequest()

        return viewOfLayout
    }

    fun initLocationUser() {
        documentReference = mStore!!.collection("users").document(userID!!)
        documentReference!!.addSnapshotListener { snapshot, e ->
            if (documentReference != null && snapshot != null) {
                if (snapshot.getString("currentLocation").toString() != "-") {
                    city_location!!.text = snapshot["currentLocation"].toString()
                    ma!!.location = snapshot["currentLocation"].toString()
                    ma!!.locationActivated = true
                }
            }
        }
    }

    private fun updateValuesFromBundle(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            if (savedInstanceState.keySet().contains(KEY_REQUESTING_LOCATION_UPDATES)) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean(
                    KEY_REQUESTING_LOCATION_UPDATES
                )
            }

            // Update the value of mCurrentLocation from the Bundle and update the UI to show the
            // correct latitude and longitude.
            if (savedInstanceState.keySet().contains(KEY_LOCATION)) {
                // Since KEY_LOCATION was found in the Bundle, we can be sure that mCurrentLocation
                // is not null.
                mCurrentLocation = savedInstanceState.getParcelable(KEY_LOCATION)!!
            }

            // Update the value of mLastUpdateTime from the Bundle and update the UI.
            if (savedInstanceState.keySet().contains(KEY_LAST_UPDATED_TIME_STRING)) {
                mLastUpdateTime = savedInstanceState.getString(KEY_LAST_UPDATED_TIME_STRING).toString()
            }
            updateLocationUI()
        }
    }

    override fun onResume() {
        super.onResume()
        // Within {@code onPause()}, we remove location updates. Here, we resume receiving
        // location updates if the user has requested them.
        if (!checkPermissions()) {
            requestPermissions()
        }
        if (ma!!.locationActivated && city_location!!.text != ma!!.location) {
            city_location!!.text = ma!!.location
        }
    }

    override fun onClick(view: View) {
        val bundle = Bundle()
        when (view.id) {
            R.id.cardivew_r -> {
                val Restaurants = "Restaurants"
                bundle.putString("Type_Event", Restaurants)
                goEventFragment(bundle)
            }
            R.id.cardivew_m -> {
                val Monuments = "Llocs d'Interès"
                bundle.putString("Type_Event", Monuments)
                goEventFragment(bundle)
            }
            R.id.cardivew_o -> {
                val Oci = "Oci"
                bundle.putString("Type_Event", Oci)
                goEventFragment(bundle)
            }
            R.id.cardivew_n -> {
                val Nocturn = "Nocturn"
                bundle.putString("Type_Event", Nocturn)
                goEventFragment(bundle)
            }
            R.id.search_location -> {
                if (!mRequestingLocationUpdates!!) {
                    mRequestingLocationUpdates = true
                    startLocationUpdates()
                }
                ma!!.locationActivated = true
            }
        }
    }

    fun goEventFragment(bundle: Bundle?) {
        val selectedFragment: Fragment = EventFragment()
        selectedFragment.arguments = bundle
        requireFragmentManager().beginTransaction().replace(R.id.fragment_main, selectedFragment).commit()
    }

    override fun onPause() {
        super.onPause()

        // Remove location updates to save battery.
        stopLocationUpdates()
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        // Begin by checking if the device has the necessary location settings.
        mSettingsClient!!.checkLocationSettings(mLocationSettingsRequest)
            .addOnSuccessListener(requireActivity()) {
                Log.i(TAG, "All location settings are satisfied.")
                mFusedLocationClient!!.requestLocationUpdates(mLocationRequest,mLocationCallback, Looper.myLooper())
            }
            .addOnFailureListener(requireActivity()) { e ->
                val statusCode = (e as ApiException).statusCode
                when (statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                        Log.i(
                            TAG, "Location settings are not satisfied. Attempting to upgrade " +
                                    "location settings "
                        )
                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the
                            // result in onActivityResult().
                            val rae = e as ResolvableApiException
                            rae.startResolutionForResult(activity, REQUEST_CHECK_SETTINGS)
                        } catch (sie: SendIntentException) {
                            Log.i(TAG, "PendingIntent unable to execute request.")
                        }
                    }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                        val errorMessage = "Location settings are inadequate, and cannot be " +
                                "fixed here. Fix in Settings."
                        Log.e(TAG, errorMessage)
                        Toast.makeText(activity, errorMessage, Toast.LENGTH_LONG).show()
                        mRequestingLocationUpdates = false
                    }
                }
                updateLocationUI()
            }
    }

    private fun createLocationCallback() {
        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                Toast.makeText(context,"LOCATION-RESULT: " + locationResult.lastLocation.toString(), Toast.LENGTH_SHORT).show()
                mCurrentLocation = locationResult.lastLocation
                mLastUpdateTime = DateFormat.getTimeInstance().format(Date())
                updateLocationUI()
            }
        }
    }

    private fun updateLocationUI() {
        if (mCurrentLocation != null) {
            val geocoder = Geocoder(context, Locale.getDefault())
            try {
                val addresses = geocoder.getFromLocation(
                    mCurrentLocation!!.latitude, mCurrentLocation!!.longitude, 1
                )
                val obj = addresses[0]
                val add = obj.locality
                takeOfAccents(add)
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun createLocationRequest() {
        mLocationRequest = LocationRequest()
        mLocationRequest!!.interval = UPDATE_INTERVAL_IN_MILLISECONDS
        mLocationRequest!!.fastestInterval = FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS
        mLocationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    private fun buildLocationSettingsRequest() {
        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(mLocationRequest!!)
        mLocationSettingsRequest = builder.build()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CHECK_SETTINGS -> when (resultCode) {
                Activity.RESULT_OK -> Log.i(
                    TAG,
                    "User agreed to make required location settings changes."
                )
                Activity.RESULT_CANCELED -> {
                    Log.i(TAG, "User chose not to make required location settings changes.")
                    mRequestingLocationUpdates = false
                    updateLocationUI()
                }
            }
        }
    }

    private fun requestPermissions() {
        val permissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        if (ContextCompat.checkSelfPermission(
                requireActivity().applicationContext,
                FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            if (ContextCompat.checkSelfPermission(
                    requireActivity().applicationContext,
                    COURSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
            } else {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE
                )
            }
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                permissions,
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun checkPermissions(): Boolean {
        val permissionState = ActivityCompat.checkSelfPermission(
            requireActivity(),
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        return permissionState == PackageManager.PERMISSION_GRANTED
    }

    private fun stopLocationUpdates() {
        if (!mRequestingLocationUpdates!!) {
            Log.d(TAG, "stopLocationUpdates: updates never requested, no-op.")
            return
        }

        // It is a good practice to remove location requests when the activity is in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.
        mFusedLocationClient!!.removeLocationUpdates(mLocationCallback)
            .addOnCompleteListener(requireActivity()) { mRequestingLocationUpdates = false }
    }

    /*
    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        savedInstanceState.putBoolean(KEY_REQUESTING_LOCATION_UPDATES, mRequestingLocationUpdates!!)
        savedInstanceState.putParcelable(KEY_LOCATION, mCurrentLocation)
        savedInstanceState.putString(KEY_LAST_UPDATED_TIME_STRING, mLastUpdateTime)
        super.onSaveInstanceState(savedInstanceState)
    }
    */

    fun takeOfAccents(text: String?) {
        var string = Normalizer.normalize(text, Normalizer.Form.NFD)
        string = string.replace("[^\\p{ASCII}]".toRegex(), "")
        if (string != ma!!.location) {
            city_location!!.text = string
            ma!!.location = string
            actualizarInfoUser()
            ma!!.sendOnMainChannel(viewOfLayout)
        }
        stopLocationUpdates()
    }

    fun actualizarInfoUser() {
        //First we have to subscribe and unsubscribe from Topics
        if (city_location!!.text.toString() != "-") {
            FirebaseMessaging.getInstance().unsubscribeFromTopic(city_location!!.text.toString())
        }
        checkUserInfo()
        val currentLocation = ma!!.location
        val user: MutableMap<String, Any?> = HashMap()
        user["currentLocation"] = currentLocation
        documentReference!![user] = SetOptions.merge()
        FirebaseMessaging.getInstance().subscribeToTopic(ma!!.location!!)
        checkUserInfo()
    }

    fun checkUserInfo() {
        val document = mStore!!.collection("users").document(
            userID!!
        )
        document.addSnapshotListener { value, error ->
            Log.d("USER-NAME", value!!.getString("name") + " " + value.getString("surname"))
            Log.d("USER-CURRENT_LOCATION", value.getString("currentLocation")!!)
        }
    }

    companion object {
        private const val UPDATE_INTERVAL_IN_MILLISECONDS: Long = 10000
        private const val FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2
        private val TAG = MainActivity::class.java.simpleName
        private const val REQUEST_CHECK_SETTINGS = 0x1
        private const val LOCATION_PERMISSION_REQUEST_CODE = 34
        private const val FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION
        private const val COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION
        private const val KEY_REQUESTING_LOCATION_UPDATES = "requesting-location-updates"
        private const val KEY_LOCATION = "location"
        private const val KEY_LAST_UPDATED_TIME_STRING = "last-updated-time-string"
    }
}