package com.yabaze.trackinghelperlibrary

import android.Manifest
import android.app.Activity
import android.content.IntentSender.SendIntentException
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import java.io.IOException

class GoogleMapsFunctionality(private val activity: Activity, private val google_location_settings: GoogleLocationSettings) {

    var settingsClient: SettingsClient? = null
    var locationRequest: LocationRequest? = null
    var locationSettingsRequest: LocationSettingsRequest? = null

    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private var locationCallback: LocationCallback? = null

    var PLAY_SERVICES_VERSION_CHECK_REQUEST = 123

    var intervalTime = 1000
    var fastIntervalTime = 1000 / 2

    fun displayLocationSettingsRequest() {

        if (checkPlayServices()) {

            locationRequest = LocationRequest.create().apply {
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                interval = intervalTime.toLong()
                fastestInterval = fastIntervalTime.toLong()
            }

            locationSettingsRequest = LocationSettingsRequest.Builder().apply {

                addLocationRequest(locationRequest!!)

            }.build()

            settingsClient = LocationServices.getSettingsClient(activity.applicationContext).apply {

                checkLocationSettings(locationSettingsRequest).apply {
                    addOnSuccessListener(
                            activity
                    ) {
                        Log.i(
                                TAG,
                                "All location settings are satisfied."
                        )
                        google_location_settings.onSuccessLocationSettings()
                    }
                    addOnFailureListener(activity) { e: Exception ->
                        var errorMessage: String? = null

                        when ((e as ApiException).statusCode) {

                            LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                                errorMessage =
                                        "Location settings are not satisfied. Attempting to upgrade location settings "
                                try {
                                    val rae = e as ResolvableApiException
                                    rae.startResolutionForResult(
                                            activity,
                                            REQUEST_CHECK_SETTINGS
                                    )
                                } catch (sie: SendIntentException) {
                                    Log.i(
                                            TAG,
                                            "PendingIntent unable to execute request."
                                    )
                                }
                            }
                            LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> errorMessage =
                                    "Location settings are inadequate, and cannot be " +
                                            "fixed here. Fix in Settings."
                        }
                        if (errorMessage == null) {
                            errorMessage = "Enable Location in Setting"
                        }
                        google_location_settings.onFailureLocationSettings(errorMessage)
                    }
                }
            }

        } else {
            //   dep_customAlertDialogs.displayInfoAlertDialog(activity,"Information","update Google play services");
        }
    }

    private fun checkPlayServices(): Boolean {
        val apiAvailability = GoogleApiAvailability
                .getInstance()
        val resultCode = apiAvailability.isGooglePlayServicesAvailable(activity)
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(activity, resultCode,
                        PLAY_SERVICES_VERSION_CHECK_REQUEST).show()
            } else {
                Log.i(TAG, "This device is not supported.")
                activity.finish()
            }
            return false
        }
        return true
    }

    companion object {
        const val REQUEST_CHECK_SETTINGS = 0x1
        private val TAG = GoogleMapsFunctionality::class.java.simpleName
    }

    interface GoogleLocationSettings {
        fun onSuccessLocationSettings()
        fun onFailureLocationSettings(errorMessage: String?)
        fun onLocationUpdates(locationResult: LocationResult?)
    }

    fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                        activity,
                        Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        activity,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity)
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                google_location_settings.onLocationUpdates(locationResult = locationResult)
            }
        }
        fusedLocationProviderClient!!.requestLocationUpdates(
                locationRequest,
                locationCallback,  //mLocationRequest, locationCallback,
                Looper.getMainLooper()
        )
    }

    fun removeLocationUpdates() {

        if (locationCallback != null && fusedLocationProviderClient != null) {
            fusedLocationProviderClient!!.removeLocationUpdates(locationCallback)
        }
    }

    fun getLocationFromAddress(strAddress: String?): LatLng? {
        val coder = Geocoder(activity)
        val address: List<Address>?
        var p1: LatLng? = null
        try {
            address = coder.getFromLocationName(strAddress, 5)
            if (address == null || address.isEmpty()) {
                return null
            }
            val location: Address = address[0]
            p1 = LatLng(location.latitude, location.longitude)
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
        return p1
    }

}