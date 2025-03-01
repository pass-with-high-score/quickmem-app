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


    private var currentMediaPlayer: MediaPlayer? = null

    fun playAudio(context: Context, audioFile: File, onCompletion: () -> Unit) {
        try {
            currentMediaPlayer?.apply {
                if (isPlaying) stop()
                release()
            }

            val mediaPlayer = MediaPlayer().apply {
                setDataSource(context, Uri.fromFile(audioFile))
                prepare()
                start()
            }

            currentMediaPlayer = mediaPlayer

            mediaPlayer.setOnCompletionListener {
                mediaPlayer.release()
                currentMediaPlayer = null
                onCompletion()
            }
        } catch (e: Exception) {
            Timber.e(e)
        }
    }
}