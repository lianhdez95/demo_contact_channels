package com.example.another_contact_channel

import android.app.Activity
import android.os.Build
import androidx.annotation.RequiresApi
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class DeviceInfoManager(binaryMessenger: BinaryMessenger, private val activity: Activity) : MethodCallHandler {

    companion object {
        private val REQUEST_DEVICE_INFO_CODE = 154
        private var result: MethodChannel.Result? = null
        private val DEVICE_INFO_CHANNEL = "com.example.device.info"
    }

    private val permissionHandler: PermissionHandlerManager = PermissionHandlerManager()
    init {
        MethodChannel(binaryMessenger, DEVICE_INFO_CHANNEL).setMethodCallHandler(this)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        when(call.method){
            "getAPIInfo" ->{
                if (permissionHandler.hasReadPhonePermissions(activity)){
                    val api = getAPIInfo()
                    result.success(api)
                } else {
                    permissionHandler.requestReadPhonePermissions(activity, REQUEST_DEVICE_INFO_CODE)
                }
            }

            "getAndroidVersionInfo" -> {
                if (permissionHandler.hasReadPhonePermissions(activity)){
                    val android = getAndroidVersionInfo()
                    result.success(android)
                } else {
                    permissionHandler.requestReadPhonePermissions(activity, REQUEST_DEVICE_INFO_CODE)
                }
            }

            "getArchitectureInfo" -> {
                if (permissionHandler.hasReadPhonePermissions(activity)){
                    val architecture = getArchitectureInfo()
                    result.success(architecture)
                } else {
                    permissionHandler.requestReadPhonePermissions(activity, REQUEST_DEVICE_INFO_CODE)
                }
            }
            else -> result.notImplemented()
        }
    }

    private fun getAPIInfo(): String{
        return Build.VERSION.SDK_INT.toString()
    }

    private fun getAndroidVersionInfo(): String{
        return Build.VERSION.RELEASE
    }

    private fun getArchitectureInfo(): String {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val supportedABIs = Build.SUPPORTED_ABIS
            if (supportedABIs.isNotEmpty()) {
                return supportedABIs[0]
            }
        }

        return "Unknown"
    }

//    private fun hasReadPhonePermissions(): Boolean{
//        val permission = android.Manifest.permission.READ_PHONE_STATE
//        return ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED
//    }
//
//    private fun requestReadPhonePermissions(){
//        ActivityCompat.requestPermissions(
//            activity,
//            arrayOf(android.Manifest.permission.READ_PHONE_STATE),
//            REQUEST_DEVICE_INFO_CODE
//        )
//    }
}