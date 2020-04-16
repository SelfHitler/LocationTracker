package com.yabaze.trackinghelperlibrary

import android.location.Location
import android.os.Build

class LocationDetail(
    var location: Location?,
    var previousLocation: Location
) {

    fun getCalculatedSpeed(): Double {
        return if (location == null) {
            HaversineAlgorithm().HaversineInKM(
                previousLocation.latitude,
                previousLocation.longitude,
                previousLocation.latitude,
                previousLocation.longitude
            )
        } else {
            HaversineAlgorithm().HaversineInKM(
                previousLocation.latitude,
                previousLocation.longitude,
                location!!.longitude,
                location!!.longitude
            )
        }
    }

    val gpsDefaultSpeed: Double
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            location?.speedAccuracyMetersPerSecond?.toDouble()?:0.0
        } else {
            location?.speed?.toDouble()?:0.0
        }

}
