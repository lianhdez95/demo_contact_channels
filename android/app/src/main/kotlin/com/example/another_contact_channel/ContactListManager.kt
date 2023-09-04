package com.example.another_contact_channel

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.provider.ContactsContract
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler

class ContactListManager(
    private val binaryMessenger: BinaryMessenger,
    private val context: Context
) : MethodCallHandler {
    companion object {
        const val READ_CONTACTS_PERMISSION_CODE = 123
        private val CONTACT_CHANNEL = "com.example.contacts"
    }

    init {
        MethodChannel(binaryMessenger, CONTACT_CHANNEL).setMethodCallHandler(this)
    }


    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        when (call.method) {
            "getContacts" -> {
                if (this.hasReadContactsPermission()) {
                    val contacts = this.getContacts()
                    result.success(contacts)
                } else {
                    result.error(
                        "PERMISSION_DENIED",
                        "Permission denied for reading contacts",
                        null
                    )
                }
            }
        }
    }

    fun hasReadContactsPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_CONTACTS
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun requestReadContactsPermission(activity: Activity, requestCode: Int) {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(Manifest.permission.READ_CONTACTS),
            requestCode
        )
    }

    fun onRequestPermissionsResult(
        requestCode: Int,
        grantResults: IntArray,
        flutterEngine: FlutterEngine
    ) {
        if (requestCode == READ_CONTACTS_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso concedido, puedes acceder a los contactos
                // Vuelve a llamar al método para obtener los contactos
                val contacts = getContacts()
                val channel = MethodChannel(
                    flutterEngine.dartExecutor.binaryMessenger,
                    "com.example.contacts"
                )
                channel.invokeMethod("getContacts", contacts)
            } else {
                Toast.makeText(context, "Permiso no concedido", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("Range")
    fun getContacts(): List<String> {
        val contacts = mutableListOf<String>()
        val contentResolver: ContentResolver = context.contentResolver
        val cursor: Cursor? = contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            null,
            null,
            null
        )
        cursor?.use {
            while (it.moveToNext()) {
                val displayName =
                    it.getString(it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))

                val contactId = it.getString(it.getColumnIndex(ContactsContract.Contacts._ID))

                val phoneCursor = contentResolver.query(
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
}