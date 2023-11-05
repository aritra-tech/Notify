package com.aritra.notify.utils

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.aritra.notify.services.reciever.ReminderNoteNotificationBroadcast

fun getNoteAlarmPendingIntent(
    context: Context,
    id: Int,
    flag: Int = PendingIntent.FLAG_UPDATE_CURRENT,
): PendingIntent? {
    return PendingIntent.getBroadcast(
        context,
        id,
        Intent(context, ReminderNoteNotificationBroadcast::class.java).apply {
            putExtra(Const.NOTE_ID_EXTRA, id)
        },
        flag or PendingIntent.FLAG_IMMUTABLE
    )
}
