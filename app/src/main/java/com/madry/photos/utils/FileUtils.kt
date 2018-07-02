package com.madry.photos.utils

import android.content.Context
import android.os.Environment
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/* package */ const val DATE_FORMAT = "yyyyMMdd_HHmmss"
/* package */ const val FILE_SUFIX = ".jpg"

class FileUtils {
    fun createFile(context: Context) : File? {
        val timeStamp = SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(Date())
        val imageFileName = timeStamp + "_"
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(imageFileName, FILE_SUFIX, storageDir)

        return image
    }
}