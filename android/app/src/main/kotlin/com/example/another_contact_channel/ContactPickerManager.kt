package com.example.another_contact_channel

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler

class ContactPickerManager(
    private val binaryMessenger: BinaryMessenger,
    private val activity: Activity
) : MethodCallHandler {


    private val CONTACT_PICKER_REQUEST = 123
    private var result: MethodChannel.Result? = null
    private val CONTACT_PICKER_CHANNEL = "com.example.contact_picker"

    init {
        MethodChannel(binaryMessenger, CONTACT_PICKER_CHANNEL).setMethodCallHandler(this)
    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {

        when (call.method) {
            "openContactPicker" -> {
                openContactPicker(result)
            }

            else -> result.notImplemented()
        }
    }

    fun openContactPicker(result: MethodChannel.Result) {
        val intent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
        activity.startActivityForResult(intent, CONTACT_PICKER_REQUEST)
        this.result = result
    }

    @SuppressLint("Range")
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CONTACT_PICKER_REQUEST && resultCode == Activity.RESULT_OK) {
            val contactsList = mutableListOf<String>()
            val contactData = data?.data

            contactData?.let { contactUri ->
                val projection = arrayOf(
                    ContactsContract.Contacts.DISPLAY_NAME,
                    ContactsContract.Contacts._ID
                )
                val cursor: Cursor? = activity.contentResolver.query(
                    contactUri,
                    projection,
                    null,
                    null,
                    null
                )
                cursor?.let {
                    if (it.moveToFirst()) {
                        val displayNameIndex: Int =
                            it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
                        if (displayNameIndex != -1) {
                            val displayName: String? = it.getString(displayNameIndex)
                            displayName?.let {
                                contactsList.add(displayName)
                            }
                        }

                        val contactIdIndex = it.getColumnIndex(ContactsContract.Contacts._ID)
                        val contactId = it.getString(contactIdIndex)

                        val phoneProjection = arrayOf(
                            ContactsContract.CommonDataKinds.Phone.NUMBER
                        )
                        val phoneCursor = activity.contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            phoneProjection,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            arrayOf(contactId),
                            null
                        )
                        phoneCursor?.let { phone ->
                            if (phone.moveToFirst()) {
                                val phoneNumberIndex: Int =
                                    phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                                if (phoneNumberIndex != -1) {
                                    val phoneNumber: String? = phone.getString(phoneNumberIndex)
                                    phoneNumber?.let {
                                        contactsList.add(phoneNumber)
                                    }
                                }
                            }
                            phone.close()
                        }
                    }
                    it.close()
                }
            }

            result?.success(contactsList)
        } else {
            result?.success(null)
        }
    }

    fun hasReadContactsPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            activity,
            android.Manifest.permission.READ_CONTACTS
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun requestReadContactsPermission() {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(android.Manifest.permission.READ_CONTACTS),
            READ_CONTACTS_PERMISSION_CODE
        )
    }

    companion object {
        const val READ_CONTACTS_PERMISSION_CODE =
            1234 // Reemplaza con tu código de permiso personalizado
    }
}


