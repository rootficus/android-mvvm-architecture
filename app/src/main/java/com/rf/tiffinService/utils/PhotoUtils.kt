package com.rf.tiffinService.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.StrictMode
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.rf.tiffinService.BuildConfig
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

/**
 * Akash.Singh
 * MOHALI.
 */
object PhotoUtils {
    const val PROFILE_FOLDER_NAME = "inventory_photo"


    fun createCapturedPhoto(
        context: Context,
        takePictureIntent: Intent,
        profileImageName: String
    ): File {
        val folder = File(context.filesDir.toString(), PROFILE_FOLDER_NAME)
        if (!folder.exists()) {
            folder.mkdir()
        }
        takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        takePictureIntent.putExtra(
            MediaStore.EXTRA_OUTPUT,
            getFileManager(context, profileImageName)
        )
        return File(folder, profileImageName)
    }


    private fun getFileManager(context: Context, fileName: String): Uri? {
        val policy = StrictMode.VmPolicy.Builder().build()
        StrictMode.setVmPolicy(policy)
        val path: String = context.filesDir.toString() + "/$PROFILE_FOLDER_NAME"
        return FileProvider.getUriForFile(
            context, BuildConfig.APPLICATION_ID + ".fileprovider", File("$path/$fileName")
        )
    }

    fun savedFileImage(context: Context, uri: Uri, profileImageName: String): String {
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        inputStream.use { inputStream ->
            val folder = File(context.filesDir.toString(), PROFILE_FOLDER_NAME)
            if (!folder.exists()) {
                folder.mkdir()
            }
            val file = File(folder, profileImageName)
            FileOutputStream(file).use { output ->
                val buffer = ByteArray(4 * 1024) // or other buffer size
                var read = 0
                while (inputStream?.read(buffer).also {
                        if (it != null) {
                            read = it
                        }
                    } != -1) {
                    output.write(buffer, 0, read)
                }
                output.flush()
            }
            return file.absolutePath
        }
    }
}