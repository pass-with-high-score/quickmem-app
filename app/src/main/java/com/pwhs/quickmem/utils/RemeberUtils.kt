package com.pwhs.quickmem.utils

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.pwhs.quickmem.BuildConfig
import com.pwhs.quickmem.R
import timber.log.Timber
import java.io.IOException

@Composable
fun rememberImageCameraCapture(onImageCaptured: (uri: Uri) -> Unit): () -> Unit {
    val context = LocalContext.current

    // State to hold the URI for the captured image
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    // Camera launcher for taking a picture
    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success && imageUri != null) {
                onImageCaptured(imageUri!!)
            }
        }

    // Permission launcher for requesting camera permissions
    val permissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                try {
                    imageUri?.let {
                        cameraLauncher.launch(it) // Camera launch try-catch
                    }
                } catch (e: Exception) {
                    Timber.e(e)
                    Toast.makeText(
                        context,
                        context.getString(R.string.txt_failed_to_open_camera),
                        Toast.LENGTH_LONG
                    ).show()
                }
            } else {
                Toast.makeText(
                    context,
                    context.getString(R.string.txt_camera_permission_denied), Toast.LENGTH_SHORT
                ).show()
            }
        }

    return remember {
        {
            val hasPermission = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
            if (hasPermission) {
                try {
                    val file = context.createImageFile() // May throw IOException
                    imageUri = try {
                        FileProvider.getUriForFile(
                            context,
                            "${BuildConfig.APPLICATION_ID}.provider",
                            file
                        )
                    } catch (e: IllegalArgumentException) {
                        Timber.e(e)
                        Toast.makeText(
                            context,
                            context.getString(R.string.txt_failed_to_get_uri_for_the_file),
                            Toast.LENGTH_SHORT
                        ).show()
                        null
                    }
                    try {
                        imageUri?.let { cameraLauncher.launch(it) }
                    } catch (e: Exception) {
                        Timber.e(e)
                        Toast.makeText(
                            context,
                            context.getString(R.string.txt_failed_to_open_camera),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } catch (e: IOException) {
                    Timber.e(e)
                    Toast.makeText(
                        context,
                        context.getString(R.string.txt_error_creating_image_file),
                        Toast.LENGTH_LONG
                    ).show()
                } catch (e: SecurityException) {
                    Timber.e(e)
                    Toast.makeText(
                        context,
                        context.getString(R.string.txt_permission_denied),
                        Toast.LENGTH_LONG
                    ).show()
                }
            } else {
                permissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }
}
