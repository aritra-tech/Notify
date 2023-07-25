package com.aritra.notify.utils

import android.content.Context
import android.content.Intent

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