package com.fionpay.agent.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.StrictMode
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.fionpay.agent.BuildConfig
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

/**
 * Akash.Singh
 * MOHALI.
 */
object PhotoUtils {
    const val PROFILE_FOLDER_NAME= "inventory_photo"


    fun createCapturedPhoto(context: Context, takePictureIntent: Intent, PROFILE_IMAGE_NAME: String): File {
        val folder = File(context?.filesDir.toString(), PROFILE_FOLDER_NAME)
        if (!folder.exists()) {
            folder.mkdir()
        }
        takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
            getFileManager(context, PROFILE_IMAGE_NAME))
        return File(folder, PROFILE_IMAGE_NAME)
    }


    private fun getFileManager(context: Context, fileName: String): Uri? {
        val policy = StrictMode.VmPolicy.Builder().build()
        StrictMode.setVmPolicy(policy)
        val path: String = context?.filesDir.toString() + "/$PROFILE_FOLDER_NAME"
        return FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileprovider", File("$path/$fileName")
        )
    }

    fun savedFileImage(context: Context,uri: Uri, PROFILE_IMAGE_NAME: String): String {
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        inputStream.use { inputStream ->
            val fileName = PROFILE_IMAGE_NAME
            val folder = File(context?.filesDir.toString(), PROFILE_FOLDER_NAME)
            if (!folder.exists()) {
                folder.mkdir()
            }
            val file = File(folder, fileName)
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