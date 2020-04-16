package com.yabaze.locationtracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.yabaze.trackinghelperlibrary.PermissionCheck

class MainActivity : AppCompatActivity() {

    val permissionCheck = PermissionCheck(this)
    lateinit var trackingService:TrackingService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        permissionCheck.checkPermission(permissionCheck.LOCATION_PERMISSIONS)
    }

    fun startService(view: View) {
        TrackingService.startTrackingService(this)
    }
    fun stopService(view: View) {
        //TrackingService.stopTrackingService(this)
    }


}
