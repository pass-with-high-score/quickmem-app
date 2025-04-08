package com.pwhs.quickmem.utils

import android.app.Activity
import android.app.LocaleManager
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.LocaleList
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.core.os.LocaleListCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.google.android.play.core.review.ReviewManagerFactory
import com.pwhs.quickmem.R
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.core.graphics.createBitmap

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth")

fun Context.bitmapToUri(bitmap: ImageBitmap): Uri {
    val file = File(this.cacheDir, "image_${System.currentTimeMillis()}.png")
    return try {
        val outputStream = FileOutputStream(file)
        bitmap.asAndroidBitmap().compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        outputStream.flush()
        outputStream.close()
        Uri.fromFile(file)
    } catch (e: IOException) {
        e.printStackTrace()
        Uri.EMPTY
    }
}

fun Context.uriToBitmap(uri: Uri): Bitmap {
    return try {
        val inputStream = this.contentResolver.openInputStream(uri)
        Bitmap.createBitmap(BitmapFactory.decodeStream(inputStream))
    } catch (e: IOException) {
        e.printStackTrace()
        createBitmap(0, 0)
    }
}

fun Context.changeLanguage(languageCode: String) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        this.getSystemService(LocaleManager::class.java).applicationLocales =
            LocaleList.forLanguageTags(languageCode)
    } else {
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(languageCode))
    }
}

fun Context.getLanguageCode(): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        this.getSystemService(LocaleManager::class.java).applicationLocales[0]?.toLanguageTag()
            ?.split("-")?.first() ?: "en"
    } else {
        AppCompatDelegate.getApplicationLocales()[0]?.toLanguageTag()?.split("-")?.first() ?: "en"
    }
}

fun Context.createImageFile(): File {
    try {
        // Check available space
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        if ((storageDir?.freeSpace ?: 0) < (10 * 1024 * 1024L)) {
            Timber.e(getString(R.string.txt_error_insufficient_storage))
            throw IOException(getString(R.string.txt_error_insufficient_storage))
        }

        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        return File.createTempFile(
            "JPEG_${timeStamp}_", // prefix
            ".jpg", // suffix
            storageDir // directory
        ).apply {
            // Register for deletion when app closes
            deleteOnExit()
        }
    } catch (e: IOException) {
        Timber.e(e)
        throw IOException(getString(R.string.txt_error_create_temp_file))
    }
}

fun Context.getActivity(): Activity? = when (this) {
    is ComponentActivity -> this
    is ContextWrapper -> baseContext.getActivity()
    else -> null
}

fun Context.launchInAppReview(
    onComplete: ((Boolean) -> Unit)? = null,
) {
    val activity = this.getActivity()
    if (activity != null) {
        val reviewManager = ReviewManagerFactory.create(activity)

        val request = reviewManager.requestReviewFlow()
        request.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val reviewInfo = task.result
                val flow = reviewManager.launchReviewFlow(activity, reviewInfo)
                flow.addOnCompleteListener {
                    // The flow has finished. The API doesn't indicate whether the user
                    // reviewed or not, or even whether the review dialog was shown.
                    // Therefore, no matter the result, continue with your app's flow.
                    Timber.tag("App Review process:").e("Finished")
                    onComplete?.invoke(true)
                }
            } else {
                // Log or handle error if you want to
                Timber.tag("App Review process:").e(task.exception?.message ?: "Failed")
                onComplete?.invoke(false)
            }
        }
    } else {
        Timber.tag("App Review process:").e("Failed: ActivityNotFound")
        onComplete?.invoke(false)
    }

}

fun Context.findActivity(): Activity {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) {
            return context
        }
        context = context.baseContext
    }
    throw IllegalStateException("Activity not found")
}
