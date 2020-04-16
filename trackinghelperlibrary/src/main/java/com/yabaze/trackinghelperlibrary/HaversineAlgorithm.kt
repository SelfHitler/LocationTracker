package com.yabaze.trackinghelperlibrary

class HaversineAlgorithm {

    val _eQuatorialEarthRadius = 6378.1370

    val _d2r = Math.PI / 180.0

    fun HaversineInM(
        lat1: Double,
        long1: Double,
        lat2: Double,
        long2: Double
    ): Int {
        return (1000.0 * HaversineInKM(lat1, long1, lat2, long2)).toInt()
    }

    fun HaversineInKM(
        lat1: Double,
        long1: Double,
        lat2: Double,
        long2: Double
    ): Double {
        val dlong = (long2 - long1) * _d2r
        val dlat = (lat2 - lat1) * _d2r
        val a = Math.pow(
            Math.sin(dlat / 2.0),
            2.0
        ) + (Math.cos(lat1 * _d2r) * Math.cos(lat2 * _d2r)
                * Math.pow(Math.sin(dlong / 2.0), 2.0))
        val c =
            2.0 * Math.atan2(Math.sqrt(a), Math.sqrt(1.0 - a))
        return _eQuatorialEarthRadius * c
    }
}