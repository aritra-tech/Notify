package com.aritradas.notify.services.reciever

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.aritradas.notify.domain.repository.NoteRepository
import com.aritradas.notify.services.exception.NoteNotFoundException
import com.aritradas.notify.utils.Const
import com.aritradas.notify.utils.sendReminderNote
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ReminderNoteNotificationBroadcast : BroadcastReceiver() {

    @Inject
    lateinit var notificationManager: NotificationManager

    @Inject
    lateinit var noteRepository: NoteRepository

    private val job = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + job)
    override fun onReceive(context: Context?, intent: Intent?) {
        goAsync().apply {
            kotlin.runCatching {
                coroutineScope.launch {
                    val getNoteId = intent!!.getIntExtra(Const.NOTE_ID_EXTRA, -1)
                    if (getNoteId == -1) {
                        throw NoteNotFoundException("There is Error Note not found")
                    }
                    val getNoteById = noteRepository.getNoteById(getNoteId)
                    //  Just to confirm that no errors occurred and send any notification
                    if (getNoteById != null && !getNoteById.isReminded) {
                        notificationManager.sendReminderNote(context!!, getNoteById)
                        noteRepository.updateNoteInRoom(getNoteById.copy(isReminded = true))
                    }
                }
            }.onSuccess {
                it.invokeOnCompletion {
                    // after scope is finish close broadcast receiver
                    finish()
                }
            }.onFailure {
                when (it) {
                    is NoteNotFoundException -> {
                        // NO Op for now
                    }
                    else -> {
                        // NO Op for now
                    }
                }
                finish()
            }
        }
    }
}
