package com.example.another_contact_channel

import android.app.Activity
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.core.app.ActivityCompat

class PermissionHandlerManager{

    fun hasCameraPermission(activity: Activity): Boolean{
        val permission = Manifest.permission.CAMERA
        return ContextCompat.checkSelfPermission(
            activity,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun requestCameraPermission(activity: Activity, requestCode: Int) {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(Manifest.permission.CAMERA),
            requestCode
        )
    }

    fun hasReadContactsPermission(activity: Activity): Boolean {
        return ActivityCompat.checkSelfPermission(
            activity,
            Manifest.permission.READ_CONTACTS
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun requestReadContactsPermission(activity: Activity, requestCode: Int) {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(Manifest.permission.READ_CONTACTS),
            requestCode
        )
    }

    fun hasDeviceInfoPermissions(activity: Activity): Boolean{
        val permission = android.Manifest.permission.READ_PHONE_STATE
        return ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED
    }

    fun requestDeviceInfoPermissions(activity: Activity, requestCode: Int){
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(Manifest.permission.READ_PHONE_STATE),
            requestCode
        )
    }

    fun hasGalleryPermissions(activity: Activity): Boolean{
        val permission = Manifest.permission.READ_EXTERNAL_STORAGE
        return ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED
    }

    fun requestGalleryPermissions(activity: Activity, requestCode: Int){
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            requestCode
        )
    }
    fun hasLocationPermissions(activity: Activity): Boolean {
        val fineLocationPermission = ContextCompat.checkSelfPermission(
            activity,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        val coarseLocationPermission = ContextCompat.checkSelfPermission(
            activity,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        return fineLocationPermission == PackageManager.PERMISSION_GRANTED ||
                coarseLocationPermission == PackageManager.PERMISSION_GRANTED
    }

    fun requestLocationPermissions(activity: Activity, requestCode: Int) {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            requestCode
        )
    }
}