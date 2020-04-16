package com.yabaze.locationtracker

import android.app.IntentService
import android.content.Intent
import android.content.Context
import android.location.Location
import android.os.Handler
import android.util.Log
import com.yabaze.trackinghelperlibrary.ErrorMessage
import com.yabaze.trackinghelperlibrary.LocationTracker
import com.yabaze.trackinghelperlibrary.LocationUpdate
import com.yabaze.trackinghelperlibrary.SuccessMessage

class TrackingService : IntentService("TrackingService") {

    private val locationTracker = object: LocationTracker(this){

        override fun onLocationChanged(
            networkLocation: Location?,
            networkLocationSpeed: Double,
            locationUpdate: LocationUpdate
        ) {
            Log.e("${locationUpdate.name}", networkLocation?.latitude.toString())
        }

        override fun onFailure(errorMessage: ErrorMessage) {
            Log.e("ERROR",errorMessage.name)
        }

        override fun onSuccess(successMessage: SuccessMessage) {
            Log.e("SUCCESS",successMessage.name)
        }

    }

    override fun onHandleIntent(intent: Intent?) {
        when (intent?.action) {
            ServiceAction.ACTION_START.name -> {
                handleActionStart()
            }
            ServiceAction.ACTION_STOP.name -> {
                handleActionStop()
            }
        }
    }



    /**
     * Handle action Start in the provided background thread with the provided
     * parameters.
     */
    private fun handleActionStart() {
        locationTracker.apply {
            updateInterval(2000,2000,1f,LocationUpdate.ALL)
            startLocationTracker(LocationUpdate.ALL)

            Handler().postDelayed(
                {
                    locationTracker.stopLocationTracker(LocationUpdate.ALL)
                },5000
            )

        }

    }

    /**
     * Handle action Stop in the provided background thread with the provided
     * parameters.
     */
    private fun handleActionStop() {
        locationTracker.stopLocationTracker(LocationUpdate.ALL)
    }

    companion object {
        /**
         * Starts this service to perform action Foo with the given parameters. If
         * the service is already performing a task this action will be queued.
         *
         * @see IntentService
         */
        // TODO: Customize helper method
        @JvmStatic
        fun startTrackingService(context: Context) {
            val intent = Intent(context, TrackingService::class.java).apply {
                action = ServiceAction.ACTION_START.name
            }
            context.startService(intent)
        }

        /**
         * Starts this service to perform action Baz with the given parameters. If
         * the service is already performing a task this action will be queued.
         *
         * @see IntentService
         */
        // TODO: Customize helper method
        @JvmStatic
        fun stopTrackingService(context: Context) {
            //val intent = Intent(context, TrackingService::class.java).apply {
              //  action = ServiceAction.ACTION_STOP.name
            //}
            //context.startService(intent)

        }
    }

}
