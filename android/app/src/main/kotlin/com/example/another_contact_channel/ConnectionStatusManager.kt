package com.example.another_contact_channel

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler

@Suppress("DEPRECATION")
class ConnectionStatusManager(
    private val binaryMessenger: BinaryMessenger,
    private val context: Context
) : MethodCallHandler {

    private val CONNECTION_STATUS_CHANNEL = "com.example.network"

    init {
        MethodChannel(binaryMessenger, CONNECTION_STATUS_CHANNEL).setMethodCallHandler(this)
    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        when (call.method) {

            "isMobileDataEnabled" -> {
                val isMobileDataEnabled: Boolean = isMobileDataEnabled(context)
                result.success(isMobileDataEnabled)
            }

            "isWifiEnabled" -> {
                val isWifiEnabled: Boolean = isWifiEnabled(context)
                result.success(isWifiEnabled)
            }

            "isBluetoothEnabled" -> {
                val isBluetoothEnabled: Boolean = isBluetoothEnabled()
                result.success(isBluetoothEnabled)
            }

            else -> {
                result.notImplemented()
            }
        }

    }

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

    fun isBluetoothEnabled(): Boolean {
        val bluetoothAdapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        return bluetoothAdapter.isEnabled
    }

    fun checkAndRequestConnectionPermissions(activity: Activity, requestCode: Int) {
        val permissionsToRequest: MutableList<String> = mutableListOf()

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

        // Verificar el permiso BLUETOOTH_ADMIN
        if (ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.BLUETOOTH_ADMIN
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            permissionsToRequest.add(Manifest.permission.BLUETOOTH_ADMIN)
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