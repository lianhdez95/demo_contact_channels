package com.example.another_contact_channel

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

@Suppress("DEPRECATION")
class ConnectionStatusManager()
{

    fun isMobileDataEnabled(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val networkCapabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            return networkCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true
        } else {
            val networkInfo = connectivityManager.activeNetworkInfo
            return networkInfo?.type == ConnectivityManager.TYPE_MOBILE
        }
    }

    fun isWifiEnabled(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
        return networkInfo?.isConnected ?: false
    }

    fun checkAndRequestConnectionPermissions(activity: Activity, requestCode: Int) {
        val permissionsToRequest = mutableListOf<String>()

        // Verificar el permiso ACCESS_NETWORK_STATE
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_NETWORK_STATE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            permissionsToRequest.add(Manifest.permission.ACCESS_NETWORK_STATE)
        }

        // Verificar el permiso ACCESS_WIFI_STATE
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_WIFI_STATE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            permissionsToRequest.add(Manifest.permission.ACCESS_WIFI_STATE)
        }

        // Solicitar permisos si hay permisos pendientes
        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                activity,
                permissionsToRequest.toTypedArray(),
                requestCode
            )
        }
    }
}