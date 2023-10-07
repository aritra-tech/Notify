package com.aritra.notify.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.view.View
import androidx.core.content.FileProvider
import com.aritra.notify.R
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
    createBitmapFromView(view, bitmapSize.first, bitmapSize.second).withNotNull { bitmap ->
        val uri = saveBitmapToCache(view.context, bitmap)
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "image/*"
            putExtra(Intent.EXTRA_STREAM, uri)
        }
        view.context.startActivity(Intent.createChooser(shareIntent, "Share via"))
    }
}

private fun createBitmapFromView(view: View, width: Int, height: Int): Bitmap {
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
    bitmap.compress(Bitmap.CompressFormat.PNG, 50, outputStream)
    outputStream.close()
    return FileProvider.getUriForFile(
        context,
        context.applicationContext.packageName + ".provider",
        file
    )
}

fun shareAsPdf(view: View, pdfFileName: String) {
    createBitmapFromView(view, view.width, view.height).withNotNull { bitmap ->
        saveBitmapAsPdf(view.context, bitmap, pdfFileName).let { pdfFile ->
            val uri = FileProvider.getUriForFile(
                view.context,
                "${view.context.packageName}.provider",
                pdfFile
            )
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "application/pdf"
                putExtra(Intent.EXTRA_STREAM, uri)
            }
            view.context.startActivity(Intent.createChooser(shareIntent, "Share via"))
        }
    }
}

private fun saveBitmapAsPdf(context: Context, bitmap: Bitmap, pdfFileName: String): File {
    val pdfDir = File(context.cacheDir, "pdfs").apply { mkdirs() }
    val pdfFile = File(pdfDir, "$pdfFileName.pdf")

    pdfFile.createNewFile()
    val outputStream = FileOutputStream(pdfFile)
    PdfDocument().apply {
        val pageInfo = PdfDocument.PageInfo.Builder(bitmap.width, bitmap.height, 1).create()
        val page = startPage(pageInfo)
        val canvas = page.canvas
        val paint = Paint().apply { color = Color.WHITE }
        canvas.drawPaint(paint)
        canvas.drawBitmap(bitmap, 0f, 0f, null)
        finishPage(page)
        writeTo(outputStream)
        outputStream.close()
        close()
    }

    return pdfFile
}

fun shareApp(context: Context) {
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, context.getString(R.string.on_share_message))
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, null)
    context.startActivity(shareIntent)
}
