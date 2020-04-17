package com.yabaze.trackinghelperlibrary

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*

abstract class LocationTracker(var context: Context) {

    private var mLocationRequest: LocationRequest? = null
    private var previousApiLocation: Location? = null

    private var gpsLocationManager: LocationManager? = null
    private var networkLocationManager: LocationManager? = null
    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null

    private var permissionCheck : PermissionCheck = PermissionCheck(context = context)

    val isGPSEnabled :Boolean
        get() {
            gpsLocationManager = context
                .getSystemService(Context.LOCATION_SERVICE) as LocationManager
            return gpsLocationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER)?:false
        }

    val isNetworkEnabled : Boolean
        get() {
            networkLocationManager = context
                .getSystemService(Context.LOCATION_SERVICE) as LocationManager
            return networkLocationManager?.isProviderEnabled(LocationManager.NETWORK_PROVIDER)?:false
        }

    private var updateInterval :Long = 5 * 1000
    private var fastestInterval:Long = 5 * 1000
    private var minimumDistance:Float = 1f

    private val locationCallback: LocationCallback = object : LocationCallback() {

        override fun onLocationResult(locationResult: LocationResult) {

            if (previousApiLocation == null) {
                previousApiLocation = locationResult.lastLocation
            }

            val locationDetail = LocationDetail(locationResult.lastLocation, previousApiLocation?:locationResult.lastLocation)

            val speed: Double =
                locationDetail.getCalculatedSpeed()

            //onGoogleFusedLocationChanged(locationResult.lastLocation, speed)
            onLocationChanged(locationResult.lastLocation, speed,LocationUpdate.FUSED_LOCATION)
            previousApiLocation = locationResult.lastLocation

        }
    }

    private var gpsLocation: Location? = null

    private var previousGpsLocation: Location? = null

    private var previousNetworkLocation: Location? = null

    private val gpsLocationListener: LocationListener =
        object : LocationListener {
            override fun onLocationChanged(location: Location) {
                if (previousGpsLocation == null) {
                    previousGpsLocation = location
                }
                gpsLocation = location
                val locationDetail = LocationDetail(location, previousGpsLocation?:location)
                val gpsCalculatedSpeed: Double =
                    locationDetail.getCalculatedSpeed()
                //onGpsLocationChanged(location, gpsCalculatedSpeed)
                onLocationChanged(location, gpsCalculatedSpeed,LocationUpdate.GPS)
                previousGpsLocation = location
            }

            override fun onStatusChanged(
                provider: String,
                status: Int,
                extras: Bundle
            ) {
            }

            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {
                Toast.makeText(context, "Please Enable GPS", Toast.LENGTH_SHORT).show()
            }
        }
    private val networkLocationListener: LocationListener =
        object : LocationListener {
            override fun onLocationChanged(location: Location) {
                if (previousNetworkLocation == null) {
                    previousNetworkLocation = location
                }
                val locationDetail =
                    LocationDetail(location, previousNetworkLocation?:location)
                val networkCalculatedSpeed: Double =
                    locationDetail.getCalculatedSpeed()
                //onNetworkLocationChanged(location, networkCalculatedSpeed)
                onLocationChanged(location, networkCalculatedSpeed,LocationUpdate.NETWORK)
                previousNetworkLocation = location
            }

            override fun onStatusChanged(
                provider: String,
                status: Int,
                extras: Bundle
            ) {
            }

            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {
                onFailure(ErrorMessage.NETWORK_NOT_AVAILABLE)
            }
        }

    private fun getGpsLocation() {

        if (isGPSEnabled) {
            if (gpsLocation == null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && context.checkSelfPermission(
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        onFailure(ErrorMessage.PERMISSION_DENIED)
                        return
                    }
                }
                gpsLocationManager!!.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    updateInterval,
                    minimumDistance,
                    gpsLocationListener,
                    Looper.getMainLooper()
                )
            } else {
                onSuccess(SuccessMessage.SERVICE_RUNNING)
            }
        } else {
            onFailure(ErrorMessage.GPS_NOT_AVAILABLE)
        }
    }

    private fun getNetworkLocationUpdates()
    {

        if (isNetworkEnabled) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && context.checkSelfPermission(
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return
                }
            }
            networkLocationManager!!.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                updateInterval,
                minimumDistance,
                networkLocationListener,
                Looper.getMainLooper()
            )
        } else {
            onFailure(ErrorMessage.NETWORK_NOT_AVAILABLE)
        }
    }


    private fun getFusedLocationUpdates()
    {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                onFailure(ErrorMessage.PERMISSION_DENIED)
                return
            }

            mLocationRequest = LocationRequest()
            mLocationRequest!!.interval = updateInterval
            mLocationRequest!!.fastestInterval = fastestInterval
            mLocationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
            val permission = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            if (permission == PackageManager.PERMISSION_GRANTED) {
                mFusedLocationProviderClient?.requestLocationUpdates(
                    mLocationRequest, locationCallback,  //mLocationRequest, locationCallback,
                    Looper.getMainLooper()
                )
            } else {
                onFailure(ErrorMessage.PERMISSION_DENIED)
                return
            }
        }

    fun startLocationTracker(updateInterval:Long,fastestInterval:Long , minimumDistance :Float, locationUpdate:LocationUpdate){

        if(permissionCheck.checkPermission(permissionCheck.LOCATION_PERMISSIONS)) {

            if (updateInterval < 1000 && fastestInterval < 1000) {

                onFailure(ErrorMessage.FAILED_TO_START)

                return

            } else {

                this.updateInterval = updateInterval
                this.fastestInterval = fastestInterval
                this.minimumDistance = minimumDistance

                stopLocationTracker(locationUpdate)
                startLocationTracker(locationUpdate)

            }
        } else {
            onFailure(errorMessage = ErrorMessage.PERMISSION_DENIED)
        }
    }

    fun startLocationTracker(locationUpdate: LocationUpdate){

        if(permissionCheck.checkPermission(permissionCheck.LOCATION_PERMISSIONS)) {


            when (locationUpdate) {

                LocationUpdate.GPS -> {
                    getGpsLocation()
                }
                LocationUpdate.NETWORK -> {
                    getNetworkLocationUpdates()
                }
                LocationUpdate.FUSED_LOCATION -> {
                    getFusedLocationUpdates()
                }
                LocationUpdate.ALL -> {
                    getNetworkLocationUpdates()
                    getGpsLocation()
                    getFusedLocationUpdates()
                }
            }

            onSuccess(SuccessMessage.SERVICE_STARTED)
        } else {
            onFailure(errorMessage = ErrorMessage.PERMISSION_DENIED)
        }
    }

    fun stopLocationTracker(locationUpdate: LocationUpdate) {
        when(locationUpdate){

            LocationUpdate.GPS->{

                gpsLocationManager?.removeUpdates(gpsLocationListener)?: run {
                    onFailure(ErrorMessage.FAILED_TO_STOP_UPDATE)
                    return
                }
            }
            LocationUpdate.NETWORK->{

                networkLocationManager?.removeUpdates(networkLocationListener)?: run {
                    onFailure(ErrorMessage.FAILED_TO_STOP_UPDATE)
                    return
                }
            }
            LocationUpdate.FUSED_LOCATION->{

                mFusedLocationProviderClient?.removeLocationUpdates(locationCallback)?: run {
                    onFailure(ErrorMessage.FAILED_TO_STOP_UPDATE)
                    return
                }
            }
            LocationUpdate.ALL->{
                gpsLocationManager?.removeUpdates(gpsLocationListener)?: run {
                    onFailure(ErrorMessage.FAILED_TO_STOP_UPDATE)
                    return
                }
                networkLocationManager?.removeUpdates(networkLocationListener)?: run {
                    onFailure(ErrorMessage.FAILED_TO_STOP_UPDATE)
                    return
                }
                mFusedLocationProviderClient?.removeLocationUpdates(locationCallback)?: run {
                    onFailure(ErrorMessage.FAILED_TO_STOP_UPDATE)
                    return
                }
            }
        }
        onSuccess(SuccessMessage.SERVICE_STOPPED)
    }

    protected abstract fun onLocationChanged(
        networkLocation: Location?,
        networkLocationSpeed: Double,
        locationUpdate: LocationUpdate
    )

    protected abstract fun onFailure(errorMessage : ErrorMessage)

    protected abstract fun onSuccess(successMessage : SuccessMessage)

}
