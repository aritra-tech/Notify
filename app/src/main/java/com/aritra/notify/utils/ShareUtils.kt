package com.aritra.notify.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.view.View
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream

fun shareNoteAsText(context: Context, title: String, description: String) {

    val shareMsg = "Title: $title\nNote: $description"

    val sharingIntent = Intent(Intent.ACTION_SEND)
    sharingIntent.type = "text/plain"

    sharingIntent.putExtra(
        Intent.EXTRA_TEXT,
        shareMsg
    )

    context.startActivity(Intent.createChooser(sharingIntent, "Share via"))
}
fun shareAsImage(view: View, bitmapSize: Pair<Int, Int>) {
    val bitmap = createBitmapFromView(view, bitmapSize.first, bitmapSize.second)
    if (bitmap != null) {
        val uri = saveBitmapToCache(view.context, bitmap)
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "image/*"
            putExtra(Intent.EXTRA_STREAM, uri)
        }
        view.context.startActivity(Intent.createChooser(shareIntent, "Share via"))
    }
}

private fun createBitmapFromView(view: View, width: Int, height: Int): Bitmap? {
    return Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888).also { bitmap ->
        Canvas(bitmap).apply {
            view.draw(this)
        }
    }
}

private fun saveBitmapToCache(context: Context, bitmap: Bitmap): Uri {
    val imagesDir = File(context.cacheDir, "images")
    imagesDir.mkdirs()
    val file = File(imagesDir, "note_image.png")
    val outputStream = FileOutputStream(file)
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
    outputStream.close()
    return FileProvider.getUriForFile(context, context.applicationContext.packageName + ".provider", file)
}
