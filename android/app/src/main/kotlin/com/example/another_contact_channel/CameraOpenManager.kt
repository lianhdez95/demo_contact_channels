package com.example.another_contact_channel

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.yalantis.ucrop.UCrop
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class CameraManager(
    private val binaryMessenger: BinaryMessenger,
    private val activity: Activity
) : MethodChannel.MethodCallHandler {
    companion object {
        private const val CHANNEL_NAME = "com.example.camera.open"
        private const val REQUEST_CAMERA_PERMISSION = 123
        private const val REQUEST_IMAGE_CAPTURE = 456
        private const val REQUEST_CROP_IMAGE = 789
    }

    private var result: MethodChannel.Result? = null
    private var currentPhotoPath: String? = null

    init {
        val channel = MethodChannel(binaryMessenger, CHANNEL_NAME)
        channel.setMethodCallHandler(this)
    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        when (call.method) {
            "openCamera" -> {
                this.result = result
                if (hasCameraPermission()) {
                    openCamera()
                } else {
                    requestCameraPermission()
                }
            }

            else -> {
                result.notImplemented()
            }
        }
    }

    private fun hasCameraPermission(): Boolean {
        val permission = Manifest.permission.CAMERA
        return ContextCompat.checkSelfPermission(
            activity,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(Manifest.permission.CAMERA),
            REQUEST_CAMERA_PERMISSION
        )
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val photoFile: File? = try {
            createImageFile()
        } catch (ex: IOException) {
            null
        }
        photoFile?.also {
            val photoURI: Uri = FileProvider.getUriForFile(
                activity,
                "com.example.camera.app",
                it
            )
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            currentPhotoPath =
                it.absolutePath // Establece la ruta de la foto capturada en currentPhotoPath
            activity.startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String =
            SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        )
    }

    // Llamado cuando se obtienen los resultados de la solicitud de permisos
    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CAMERA_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera()
                } else {
                    this.result?.error("PERMISSION_DENIED", "Camera permission denied.", null)
                }
            }
        }
    }

    @SuppressLint("ResourceType")
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val photoUri =
                Uri.fromFile(currentPhotoPath?.let { File(it) }) // Convierte la ruta de la foto a Uri

            val uCropOptions = UCrop.Options()
            uCropOptions.setStatusBarColor(Color.BLUE)// Aquí puedes personalizar uCrop como prefieras

            this.activity.let {
                UCrop.of(photoUri, photoUri)
                    .withOptions(uCropOptions)
                    .start(
                        it,
                        REQUEST_CROP_IMAGE
                    ) // Aquí el contexto de 'it' es equivalente a tu instancia de activity.
            } // REQUEST_CROP_IMAGE es una constante que deberías definir, al igual que REQUEST_IMAGE_CAPTURE
        } else if (requestCode == REQUEST_CROP_IMAGE && resultCode == Activity.RESULT_OK) {
            // Cuando uCrop ha terminado el recorte, graba la nueva ruta y procede como lo harías normalmente
            val resultUri = data?.let { UCrop.getOutput(it) }
            currentPhotoPath = resultUri?.path
            result?.success(currentPhotoPath)
        }
    }


}