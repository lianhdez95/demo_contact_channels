package com.example.another_contact_channel

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler

class LocationManager(
    private val binaryMessenger: BinaryMessenger,
    private val activity: Activity
) : MethodCallHandler {

    companion object {
        private const val REQUEST_LOCATION_PERMISSION_CODE = 651
        private const val LOCATION_CHANNEL = "com.example.location"
    }

    private var methodResult: MethodChannel.Result? = null
    private var locationManager: LocationManager? = null
    private val permissionHandler: PermissionHandlerManager = PermissionHandlerManager()

    init {
        MethodChannel(binaryMessenger, LOCATION_CHANNEL).setMethodCallHandler(this)
    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        methodResult = result
        when (call.method) {
            "getLocationInfo" -> {
                getCoordinates()
            }
            else -> {
                methodResult?.notImplemented()
            }
        }
    }

    private fun getCoordinates() {
        if (permissionHandler.hasLocationPermissions(activity)) {
            requestLocationUpdates()
        } else {
            permissionHandler.requestLocationPermissions(activity, REQUEST_LOCATION_PERMISSION_CODE)
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestLocationUpdates() {
        locationManager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager?.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            0L,
            0f,
            locationListener
        )
    }

    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            methodResult?.success(
                mapOf(
                    "latitude" to location.latitude,
                    "longitude" to location.longitude
                )
            )
            locationManager?.removeUpdates(this)
        }
    }

//    private fun checkLocationPermissions(): Boolean {
//        val fineLocationPermission = ContextCompat.checkSelfPermission(
//            context,
//            Manifest.permission.ACCESS_FINE_LOCATION
//        )
//        val coarseLocationPermission = ContextCompat.checkSelfPermission(
//            context,
//            Manifest.permission.ACCESS_COARSE_LOCATION
//        )
//        return fineLocationPermission == PackageManager.PERMISSION_GRANTED ||
//                coarseLocationPermission == PackageManager.PERMISSION_GRANTED
//    }
//
//    private fun requestLocationPermissions() {
//        ActivityCompat.requestPermissions(
//            context as Activity,
//            arrayOf(
//                Manifest.permission.ACCESS_FINE_LOCATION,
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            ),
//            REQUEST_LOCATION_PERMISSION_CODE
//        )
//    }

//    private fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        if (requestCode == REQUEST_LOCATION_PERMISSION_CODE) {
//            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                requestLocationUpdates()
//            } else {
//                methodResult?.error(
//                    "PERMISSION_DENIED",
//                    "Location permission denied",
//                    null
//                )
//            }
//        }
//    }


}