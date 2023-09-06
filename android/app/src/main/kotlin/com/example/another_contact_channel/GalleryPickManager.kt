package com.example.another_contact_channel

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.core.app.ActivityCompat
import com.yalantis.ucrop.UCrop
import io.flutter.embedding.android.FlutterActivity
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import java.io.File

class GalleryChannel(private val binaryMessenger: BinaryMessenger, private val activity: Activity) :
    MethodCallHandler {
    private val GALLERY_CHANNEL = "com.example.gallery.pick"
    private val GALLERY_REQUEST_CODE = 0
    private var result: Result? = null
    private val REQUEST_CROP_IMAGE = 213

    init {
        MethodChannel(binaryMessenger, GALLERY_CHANNEL).setMethodCallHandler(this)
    }

    override fun onMethodCall(call: MethodCall, result: Result) {
        when (call.method) {
            "openGallery" -> {
                this.result = result
                checkGalleryPermission(activity)
            }

            else -> result.notImplemented()
        }
    }

    private fun checkGalleryPermission(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (activity.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                openGallery(activity)
            } else {
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    GALLERY_REQUEST_CODE
                )
            }
        } else {
            openGallery(activity)
        }
    }

    private fun openGallery(activity: Activity) {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        activity.startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }

    @SuppressLint("ResourceType")
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == FlutterActivity.RESULT_OK && data != null) {
            val result = this.getResult()

            val uri = data.data
            val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = activity.contentResolver.query(uri!!, filePathColumn, null, null, null)

            if (cursor != null && cursor.moveToFirst()) {
                val columnIndex: Int = cursor.getColumnIndex(filePathColumn[0])
                val filePath: String = cursor.getString(columnIndex)
                cursor.close()

                // Generate a new destination URI for each edit
                val destinationUri =
                    Uri.fromFile(File(activity.cacheDir, "cropped_image_$requestCode.jpg"))

                // Launch UCrop for image editing
                launchUCrop(activity, Uri.fromFile(File(filePath)), destinationUri, result)
            }
        } else if (requestCode == REQUEST_CROP_IMAGE && resultCode == Activity.RESULT_OK) {
            // When UCrop has finished cropping, get the new image path and proceed as usual
            val resultUri = data?.let { UCrop.getOutput(it) }
            val filePath = resultUri?.path
            result?.success(filePath)
        }
    }

    private fun launchUCrop(
        activity: Activity,
        sourceUri: Uri,
        destinationUri: Uri,
        result: Result?
    ) {
        val uCropOptions = UCrop.Options()
        uCropOptions.setStatusBarColor(Color.BLUE)

        UCrop.of(sourceUri, destinationUri)
            .withOptions(uCropOptions)
            .start(activity, REQUEST_CROP_IMAGE)

        // Pass the result instance to UCrop
        this.result = result
    }

    fun getResult(): Result? {
        return result
    }
}