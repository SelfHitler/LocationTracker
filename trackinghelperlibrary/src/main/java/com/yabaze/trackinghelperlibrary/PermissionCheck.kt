package com.yabaze.trackinghelperlibrary

import android.Manifest.permission
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class PermissionCheck(private val context: Context) {

    var REQUIRE_PERMISSIONS = arrayOf(
        permission.ACCESS_FINE_LOCATION,
        permission.ACCESS_COARSE_LOCATION,
        permission.CAMERA,
        permission.READ_PHONE_STATE,
        permission.WRITE_EXTERNAL_STORAGE,
        permission.READ_EXTERNAL_STORAGE
    )

    var LOCATION_PERMISSIONS = arrayOf(
        permission.ACCESS_FINE_LOCATION,
        permission.ACCESS_COARSE_LOCATION
    )

    fun checkPermission(permissions: Array<String>): Boolean {
        var isGranted = checkSelfPermission(permissions)
        if (!isGranted) { // Permission is not granted
            if (RequestPermissionRationale(permissions)) {
                ActivityCompat.requestPermissions(
                    context as Activity,
                    permissions,
                    1
                )
            } else {
                ActivityCompat.requestPermissions(
                    context as Activity,
                    permissions,
                    1
                )
            }
            isGranted = checkSelfPermission(permissions)
        }
        return isGranted
    }

    private fun RequestPermissionRationale(permissions: Array<String>): Boolean {
        var permissionCheck = true
        for (permission in permissions) {
            permissionCheck =
                permissionCheck and ActivityCompat.shouldShowRequestPermissionRationale(
                    context as Activity,
                    permission
                )
        }
        return permissionCheck
    }

    private fun checkSelfPermission(permissions: Array<String>): Boolean {
        var permissionCheck = true
        for (permission in permissions) {
            permissionCheck = permissionCheck and (ContextCompat.checkSelfPermission(context, permission)
                    == PackageManager.PERMISSION_GRANTED)
        }
        return permissionCheck
    }

    companion object {
        var STORAGE_AND_CAMERA_PERMISSIONS = arrayOf(
            permission.WRITE_EXTERNAL_STORAGE,
            permission.READ_EXTERNAL_STORAGE, permission.CAMERA
        )
        var STORAGE_PERMISSIONS = arrayOf(
            permission.WRITE_EXTERNAL_STORAGE,
            permission.READ_EXTERNAL_STORAGE
        )
        var CAMERA_PERMISSIONS =
            arrayOf(permission.CAMERA)
    }

    init {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            REQUIRE_PERMISSIONS = arrayOf(
                permission.ACCESS_FINE_LOCATION,
                permission.ACCESS_COARSE_LOCATION,
                permission.ACCESS_BACKGROUND_LOCATION,
                permission.CAMERA,
                permission.READ_PHONE_STATE,
                permission.WRITE_EXTERNAL_STORAGE,
                permission.READ_EXTERNAL_STORAGE
            )
            LOCATION_PERMISSIONS = arrayOf(
                permission.ACCESS_FINE_LOCATION,
                permission.ACCESS_BACKGROUND_LOCATION,
                permission.ACCESS_COARSE_LOCATION
            )
        }
    }
}