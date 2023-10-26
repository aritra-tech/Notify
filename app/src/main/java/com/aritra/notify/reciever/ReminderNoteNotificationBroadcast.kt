package com.aritra.notify.reciever

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.aritra.notify.domain.models.Note
import com.aritra.notify.domain.repository.NoteRepository
import com.aritra.notify.utils.sendReminderNote
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ReminderNoteNotificationBroadcast:BroadcastReceiver() {

    @Inject
    lateinit var notificationManager: NotificationManager

    @Inject
    lateinit var noteRepository: NoteRepository
    override fun onReceive(context: Context?, intent: Intent?) {
        notificationManager.sendReminderNote(context!!, Note())
    }
}