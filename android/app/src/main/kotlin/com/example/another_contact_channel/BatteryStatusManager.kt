package com.example.another_contact_channel

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler

class BatteryStatusManager(
    private val binaryMessenger: BinaryMessenger,
    private val context: Context
) : MethodCallHandler {

    companion object {
        private val BATTERY_CHANNEL = "com.example.battery"
    }

    init {
        MethodChannel(binaryMessenger, BATTERY_CHANNEL).setMethodCallHandler(this)
    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        when (call.method) {
            "getBatteryStatus" -> {
                val batteryStatus: String = getBatteryStatus()
                result.success(batteryStatus)
            }

            else ->
                result.notImplemented()
        }
    }

    fun getBatteryStatus(): String {
        val batteryIntent =
            context.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        val level = batteryIntent?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
        val scale = batteryIntent?.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
        val batteryPercentage = (level?.toFloat() ?: 0f) / (scale?.toFloat() ?: 1f) * 100

        return "Battery Level: $batteryPercentage%"
    }

}