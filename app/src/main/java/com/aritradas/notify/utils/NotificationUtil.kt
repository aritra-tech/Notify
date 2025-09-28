package com.aritradas.notify.utils

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.aritradas.notify.R
import com.aritradas.notify.domain.models.Note

fun NotificationManager.sendReminderNote(context: Context, note: Note) {
    val notification = NotificationCompat.Builder(context, "1").apply {
        setSmallIcon(R.drawable.notify_icon)
        setContentTitle(note.title)
        setContentText(note.note)
        setAutoCancel(true)
    }.build()
    notify(note.id, notification)
}
