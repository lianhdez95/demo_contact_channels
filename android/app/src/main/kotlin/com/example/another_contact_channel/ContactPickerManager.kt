package com.example.another_contact_channel

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.ContactsContract
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import io.flutter.plugin.common.MethodChannel

class ContactPickerManager(private val activity: Activity) {
    private val CONTACT_PICKER_REQUEST = 123
    private var result: MethodChannel.Result? = null

    fun openContactPicker(result: MethodChannel.Result) {
        val intent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
        activity.startActivityForResult(intent, CONTACT_PICKER_REQUEST)
        this.result = result
    }

    @SuppressLint("Range")
    fun handleActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CONTACT_PICKER_REQUEST && resultCode == Activity.RESULT_OK) {
            val contactsList = mutableListOf<String>()
            val contactsData = data?.data
            val contactsCursor =
                contactsData?.let { activity.contentResolver.query(it, null, null, null, null) }

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
        const val READ_CONTACTS_PERMISSION_CODE = 1234 // Reemplaza con tu código de permiso personalizado
    }
}