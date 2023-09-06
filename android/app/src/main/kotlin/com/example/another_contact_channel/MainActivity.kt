package com.example.another_contact_channel

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine

class MainActivity : FlutterActivity() {
    private val READ_CONTACTS_PERMISSION_CODE = 123
    private val CONNECTION_PERMISSIONS_REQUEST_CODE = 1
    private val READ_PHONE_REQUEST_PERMISSION_CODE = 1

    private lateinit var contactManager: ContactListManager
    private lateinit var batteryStatus: BatteryStatusManager
    private lateinit var connectionStatus: ConnectionStatusManager
    private lateinit var contactPickerManager: ContactPickerManager
    private lateinit var cameraOpenManager: CameraManager
    private lateinit var galleryChannel: GalleryChannel

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        ContactListManager(flutterEngine!!.dartExecutor.binaryMessenger, this).also { this.contactManager = it }
//        // Verificar y solicitar permisos en tiempo de ejecución
//        if (!contactManager.hasReadContactsPermission()) {
//            contactManager.requestReadContactsPermission(this, READ_CONTACTS_PERMISSION_CODE)
//        }
//
//        BatteryStatusManager(flutterEngine!!.dartExecutor.binaryMessenger, this).also { this.batteryStatus = it }
//
//        ConnectionStatusManager(flutterEngine!!.dartExecutor.binaryMessenger, this).also { this.connectionStatus = it }
//        connectionStatus.checkAndRequestConnectionPermissions(this, CONNECTION_PERMISSIONS_REQUEST_CODE)
//
//        contactPickerManager = ContactPickerManager(flutterEngine!!.dartExecutor.binaryMessenger, this)
//        if (!contactPickerManager.hasReadContactsPermission()) {
//            contactPickerManager.requestReadContactsPermission()
//        }
//    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        //Method Channel para la obtención de los contactos para mostrarlos en el ListView
        super.configureFlutterEngine(flutterEngine)

        contactManager = ContactListManager(flutterEngine.dartExecutor.binaryMessenger, this)
        batteryStatus = BatteryStatusManager(flutterEngine.dartExecutor.binaryMessenger, this)
        connectionStatus = ConnectionStatusManager(flutterEngine.dartExecutor.binaryMessenger, this)
        contactPickerManager = ContactPickerManager(flutterEngine.dartExecutor.binaryMessenger, this)
        cameraOpenManager = CameraManager(flutterEngine.dartExecutor.binaryMessenger, this)
        galleryChannel = GalleryChannel(flutterEngine.dartExecutor.binaryMessenger, this)
    }


    @SuppressLint("Range")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        contactPickerManager.onActivityResult(requestCode, resultCode, data)
        cameraOpenManager.onActivityResult(requestCode, resultCode, data)
        galleryChannel.onActivityResult(requestCode, resultCode, data)
    }
}
