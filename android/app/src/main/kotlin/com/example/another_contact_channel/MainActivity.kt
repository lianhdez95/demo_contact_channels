package com.example.another_contact_channel

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine

class MainActivity : FlutterActivity() {

    private lateinit var contactManager: ContactListManager
    private lateinit var batteryStatus: BatteryStatusManager
    private lateinit var connectionStatus: ConnectionStatusManager
    private lateinit var contactPicker: ContactPickerManager
    private lateinit var cameraOpen: CameraManager
    private lateinit var galleryChannel: GalleryChannel
    private lateinit var deviceInfo: DeviceInfoManager
    private lateinit var locationManager: LocationManager
    private lateinit var commandManager: CommandManager


    @RequiresApi(Build.VERSION_CODES.O)
    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        //Method Channel para la obtención de los contactos para mostrarlos en el ListView
        super.configureFlutterEngine(flutterEngine)

        contactManager = ContactListManager(flutterEngine.dartExecutor.binaryMessenger, this)
        batteryStatus = BatteryStatusManager(flutterEngine.dartExecutor.binaryMessenger, this)
        connectionStatus = ConnectionStatusManager(flutterEngine.dartExecutor.binaryMessenger, this)
        contactPicker = ContactPickerManager(flutterEngine.dartExecutor.binaryMessenger, this)
        cameraOpen = CameraManager(flutterEngine.dartExecutor.binaryMessenger, this)
        galleryChannel = GalleryChannel(flutterEngine.dartExecutor.binaryMessenger, this)
        deviceInfo = DeviceInfoManager(flutterEngine.dartExecutor.binaryMessenger, this)
        locationManager = LocationManager(flutterEngine.dartExecutor.binaryMessenger, this)
        commandManager = CommandManager(flutterEngine.dartExecutor.binaryMessenger, this)
    }


    @SuppressLint("Range")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        contactPicker.onActivityResult(requestCode, resultCode, data)
        cameraOpen.onActivityResult(requestCode, resultCode, data)
        galleryChannel.onActivityResult(requestCode, resultCode, data)
    }
}
