package com.booktracker.lectio.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

object ImageUtils {
    suspend fun saveImageToInternalStorage(
        context: Context,
        uri: Uri,
        quality: Int = 80 // Compression quality (0-100)
    ): String? = withContext(Dispatchers.IO) {
        try {
            // Create a unique file name
            val fileName = "cover_${System.currentTimeMillis()}.jpg"
            val file = File(context.filesDir, fileName)

            // Decode the image into a Bitmap
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            val bitmap = inputStream?.use { BitmapFactory.decodeStream(it) }

            if (bitmap != null) {
                // Compress and save the Bitmap to internal storage
                FileOutputStream(file).use { output ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, quality, output)
                }
                file.absolutePath
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}