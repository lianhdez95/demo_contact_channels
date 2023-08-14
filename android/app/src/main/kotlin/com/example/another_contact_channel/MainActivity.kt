package com.example.another_contact_channel

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel

class MainActivity : FlutterActivity() {
    private val CONTACT_CHANNEL = "com.example.contacts"
    private val BATTERY_CHANNEL = "com.example.battery"
    private val CONTACT_PICKER_CHANNEL = "com.example.contact_picker"
    private val READ_CONTACTS_PERMISSION_CODE = 123
    private val CONNECTION_PERMISSIONS_REQUEST_CODE = 1
    private val CONTACT_PICKER_REQUEST = 123
    private var result: MethodChannel.Result? = null

    private lateinit var contactManager: ContactListManager
    private lateinit var batteryStatus: BatteryStatusManager
    private lateinit var connectionStatus: ConnectionStatusManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ContactListManager(this).also { this.contactManager = it }
        // Verificar y solicitar permisos en tiempo de ejecución
        if (!contactManager.hasReadContactsPermission()) {
            contactManager.requestReadContactsPermission(this, READ_CONTACTS_PERMISSION_CODE)
        }

        BatteryStatusManager(this).also { this.batteryStatus = it }

        ConnectionStatusManager().also { this.connectionStatus = it }
        connectionStatus.checkAndRequestConnectionPermissions(this, CONNECTION_PERMISSIONS_REQUEST_CODE)
    }

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)

        //Method Channel para la obtención de los contactos para mostrarlos en el ListView
        MethodChannel(
            flutterEngine.dartExecutor.binaryMessenger,
            CONTACT_CHANNEL
        ).setMethodCallHandler { call, result ->
            if (call.method == "getContacts") {
                if (contactManager.hasReadContactsPermission()) {
                    val contacts = contactManager.getContacts()
                    result.success(contacts)
                } else {
                    result.error(
                        "PERMISSION_DENIED",
                        "Permission denied for reading contacts",
                        null
                    )
                }
            } else {
                result.notImplemented()
            }
        }

        //Method Channel para conocer el nivel de la batería
        MethodChannel(
            flutterEngine.dartExecutor.binaryMessenger,
            BATTERY_CHANNEL
        ).setMethodCallHandler { call, result ->
            if (call.method == "getBatteryStatus") {
                val batteryStatus: String = batteryStatus.getBatteryStatus()
                result.success(batteryStatus)
            } else {
                result.notImplemented()
            }
        }

        //Method Channel para conocer el estado de conectividad del dispositivo
        MethodChannel(
            flutterEngine.dartExecutor.binaryMessenger,
            "com.example.network"
        ).setMethodCallHandler { call, result ->
            when (call.method) {
                "isMobileDataEnabled" -> {
                    val isMobileDataEnabled = connectionStatus.isMobileDataEnabled(this)
                    result.success(isMobileDataEnabled)
                }

                "isWifiEnabled" -> {
                    val isWifiEnabled = connectionStatus.isWifiEnabled(this)
                    result.success(isWifiEnabled)
                }

                else -> {
                    result.notImplemented()
                }
            }
        }

        //Method Channel para seleccionar y mostrar un contacto a partir de la aplicación de contactos del dispositivo
        MethodChannel(
            flutterEngine.dartExecutor.binaryMessenger,
            CONTACT_PICKER_CHANNEL
        ).setMethodCallHandler { call, result ->
            when (call.method) {
                "openContactPicker" -> {
                    openContactPicker(result)
                }

                else -> result.notImplemented()
            }
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        contactManager.onRequestPermissionsResult(requestCode, grantResults, flutterEngine!!)
    }


    private fun openContactPicker(result: MethodChannel.Result) {
        val intent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
        startActivityForResult(intent, CONTACT_PICKER_REQUEST)
        this.result = result
    }

    @SuppressLint("Range")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CONTACT_PICKER_REQUEST && resultCode == RESULT_OK) {
            val contactsList = mutableListOf<String>()
            val contactsData = data?.data
            val contactsCursor =
                contactsData?.let { contentResolver.query(it, null, null, null, null) }

            if (contactsCursor?.moveToFirst() == true) {
                val displayName =
                    contactsCursor.getString(contactsCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                contactsList.add(displayName)
            }

            contactsCursor?.close()
            result?.success(contactsList)
        } else {
            result?.success(null)
        }
    }
}
