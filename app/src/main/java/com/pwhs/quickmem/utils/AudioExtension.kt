package com.pwhs.quickmem.utils

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import timber.log.Timber
import java.io.File

object AudioExtension {
    fun convertByteArrayToAudio(context: Context, byteArray: ByteArray, fileName: String): File? {
        return try {
            val audioBuffer = byteArray.copyOfRange(44, byteArray.size)
            val outputFile = File(context.cacheDir, fileName)
            outputFile.writeBytes(audioBuffer)
            outputFile
        } catch (e: Exception) {
            Timber.e(e)
            null
        }
    }


    fun playAudio(context: Context, audioFile: File, onCompletion: () -> Unit) {
        try {
            val mediaPlayer = MediaPlayer().apply {
                setDataSource(context, Uri.fromFile(audioFile))
                prepare()
                start()
            }

            mediaPlayer.setOnCompletionListener {
                mediaPlayer.release()
                onCompletion()
            }
        } catch (e: Exception) {
            Timber.e(e)
        }
    }
}