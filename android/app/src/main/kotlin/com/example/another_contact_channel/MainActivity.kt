package com.example.another_contact_channel

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.BatteryManager
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugins.GeneratedPluginRegistrant

@Suppress("DEPRECATION")
class MainActivity : FlutterActivity() {
    private val CONTACT_CHANNEL = "com.example.contacts"
    private val BATTERY_CHANNEL = "com.example.battery"
    private val CONTACT_PICKER_CHANNEL = "com.example.contact_picker"
    private val READ_CONTACTS_PERMISSION_CODE = 123
    private val CONNECTION_PERMISSIONS_REQUEST_CODE = 1
    private val CONTACT_PICKER_REQUEST = 123
    private var result: MethodChannel.Result? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkAndRequestConnectionPermissions()
        // Verificar y solicitar permisos en tiempo de ejecución
        if (!hasReadContactsPermission()) {
            requestReadContactsPermission()
        }


    }

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)

        MethodChannel(
            flutterEngine.dartExecutor.binaryMessenger,
            CONTACT_CHANNEL
        ).setMethodCallHandler { call,
                                 result ->
            if (call.method == "getContacts") {
                if (hasReadContactsPermission()) {
                    val contacts = getContacts()
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


        MethodChannel(
            flutterEngine.dartExecutor.binaryMessenger,
            BATTERY_CHANNEL
        ).setMethodCallHandler { call, result ->
            if (call.method == "getBatteryStatus") {
                val batteryStatus = getBatteryStatus()
                result.success(batteryStatus)
            } else {
                result.notImplemented()
            }
        }

        MethodChannel(
            flutterEngine.dartExecutor.binaryMessenger,
            "com.example.network"
        ).setMethodCallHandler { call, result ->
            when (call.method) {
                "isMobileDataEnabled" -> {
                    val isMobileDataEnabled = isMobileDataEnabled(this)
                    result.success(isMobileDataEnabled)
                }

                "isWifiEnabled" -> {
                    val isWifiEnabled = isWifiEnabled(this)
                    result.success(isWifiEnabled)
                }

                else -> result.notImplemented()
            }
        }
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

    private fun hasReadContactsPermission(): Boolean {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) ==
                PackageManager.PERMISSION_GRANTED
    }

    private fun requestReadContactsPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.READ_CONTACTS),
            READ_CONTACTS_PERMISSION_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == READ_CONTACTS_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso concedido, puedes acceder a los contactos
                // Vuelve a llamar al método para obtener los contactos
                val contacts = getContacts()
                val channel = MethodChannel(flutterEngine!!.dartExecutor.binaryMessenger, CONTACT_CHANNEL)
                channel.invokeMethod("getContacts", contacts)
            } else {
                Toast.makeText(this, "Permiso no concedido", Toast.LENGTH_SHORT).show()
            }
        }
        if (requestCode == CONNECTION_PERMISSIONS_REQUEST_CODE) {
            // Verificar si se concedieron todos los permisos solicitados
            if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                // Se concedieron los permisos, realizar las acciones necesarias
            } else {
                Toast.makeText(this, "Permiso no concedido", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("Range")
    private fun getContacts(): List<String> {
        val contacts = mutableListOf<String>()
        val contentResolver: ContentResolver = applicationContext.contentResolver
        val cursor =
            contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null)
        cursor?.use {
            while (it.moveToNext()) {
                val displayName =
                    it.getString(it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))

                val contactId = it.getString(it.getColumnIndex(ContactsContract.Contacts._ID))

                val phoneCursor =
                    contentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        arrayOf(contactId),
                        null
                    )

                phoneCursor?.use { phone ->
                    while (phone.moveToNext()) {
                        val phoneNumber =
                            phone.getString(
                                phone.getColumnIndex(
                                    ContactsContract.CommonDataKinds.Phone.NUMBER
                                )
                            )
                        contacts.add("$displayName - $phoneNumber")
                    }
                }
            }
        }
        return contacts
    }

    private fun getBatteryStatus(): String {
        val batteryIntent =
            applicationContext.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        val level = batteryIntent?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
        val scale = batteryIntent?.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
        val batteryPercentage = (level?.toFloat() ?: 0f) / (scale?.toFloat() ?: 1f) * 100

        return "Battery Level: $batteryPercentage%"
    }

    private fun isMobileDataEnabled(context: Context): Boolean {
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

    private fun isWifiEnabled(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
        return networkInfo?.isConnected ?: false
    }

    private fun checkAndRequestConnectionPermissions() {
        val permissionsToRequest = mutableListOf<String>()

        // Verificar el permiso ACCESS_NETWORK_STATE
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            permissionsToRequest.add(Manifest.permission.ACCESS_NETWORK_STATE)
        }

        // Verificar el permiso ACCESS_WIFI_STATE
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_WIFI_STATE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            permissionsToRequest.add(Manifest.permission.ACCESS_WIFI_STATE)
        }

        // Solicitar permisos si hay permisos pendientes
        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                permissionsToRequest.toTypedArray(),
                CONNECTION_PERMISSIONS_REQUEST_CODE
            )
        }
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
