package com.example.testprogram.ui

import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import android.provider.MediaStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class FileManager {
    suspend fun saveDrawing(context:Context,bitmap: Bitmap) = withContext(Dispatchers.IO){
        try {
            val fileName = "drawing.png"
            val folder = File(
                Environment.getExternalStorageDirectory().toString() + File.separator + "Drawings"
            )
            if (!folder.exists()) {
                folder.mkdirs()
            }
            val file = File(folder, fileName)
            val outputStream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.flush()
            outputStream.close()

            // Make the image visible in the gallery
            MediaStore.Images.Media.insertImage(
                context.contentResolver,
                file.absolutePath,
                fileName,
                "Drawing"
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}