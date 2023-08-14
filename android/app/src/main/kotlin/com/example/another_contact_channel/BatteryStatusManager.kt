package com.example.another_contact_channel

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager

class BatteryStatusManager(private val context: Context) {

    fun getBatteryStatus(): String {
        val batteryIntent =
            context.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        val level = batteryIntent?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
        val scale = batteryIntent?.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
        val batteryPercentage = (level?.toFloat() ?: 0f) / (scale?.toFloat() ?: 1f) * 100

        return "Battery Level: $batteryPercentage%"
    }

}