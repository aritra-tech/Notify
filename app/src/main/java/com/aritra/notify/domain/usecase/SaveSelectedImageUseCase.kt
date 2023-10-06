package com.aritra.notify.domain.usecase

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.core.content.FileProvider
import java.io.File

object SaveSelectedImageUseCase {
    const val DIRECTORY = "image"

    /**
     * Returns the image file in the cache directory
     */
    fun image(context: Context, id: Int, index: Int) = File(
        File(context.externalCacheDir, DIRECTORY).apply {
            if (!exists()) {
                mkdirs()
            }
        },
        "image_${id}_($index).webp"
    )

    private fun imageFileUri(context: Context, uri: Uri, noteId: Int, index: Int): Uri? = try {
        // copy the image to cache directory because opening the
        // image uri after app restart doesn't work for external storage uri on android 11 and above
        val image = image(context, noteId, index)
        val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ImageDecoder.decodeBitmap(
                ImageDecoder.createSource(
                    context.contentResolver,
                    uri
                )
            )
        } else {
            MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
        }
        // compress the image to 80% webp quality before saving
        bitmap.compress(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                Bitmap.CompressFormat.WEBP_LOSSY
            } else {
                Bitmap.CompressFormat.WEBP
            },
            80,
            image.outputStream()
        )
        FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            image
        )
    } catch (_: Exception) {
        null
    }

    /**
     * Saves the selected image to the cache directory and returns the uri of the saved image
     */
    operator fun invoke(context: Context, uris: List<Uri>, noteId: Int): List<Uri?> {
        val imageFileUris = mutableListOf<Uri?>()
        uris.forEachIndexed { index, uri ->
            imageFileUris.add(imageFileUri(context, uri, noteId, index))
        }
        return imageFileUris.toList()
    }
}
