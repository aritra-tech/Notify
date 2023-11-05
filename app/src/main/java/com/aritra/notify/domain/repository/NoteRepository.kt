package com.aritra.notify.domain.repository

import android.app.Application
import com.aritra.notify.services.alarm.AlarmInfo
import com.aritra.notify.services.alarm.AlarmScheduler
import com.aritra.notify.data.dao.NoteDao
import com.aritra.notify.data.db.NoteDatabase
import com.aritra.notify.domain.models.Note
import com.aritra.notify.utils.triggerDateTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteRepository @Inject constructor(application: Application, private val alarmScheduler: AlarmScheduler) {

    private var noteDao: NoteDao

    init {
        val database = NoteDatabase.getInstance(application)
        noteDao = database.noteDao()
        // TODO: add here to check alarms again
    }

    suspend fun deleteNoteById(noteId: Int) {
        noteDao.deleteNoteById(noteId)
        alarmScheduler.cancelAlarm(AlarmInfo(noteId, 0))
    }

    suspend fun checkAlarms() {
        val getNoteWithReminder = noteDao.getAllNotes().filter {
            it.reminderDateTime != null && !it.isReminded && !it.isMovedToTrash
        }
        checkIfRemindedNoteIsAfterCurrentDateTime(noteDao, getNoteWithReminder)

        getNoteWithReminder.filter {
            !it.isReminded
        }.forEach {
            alarmScheduler.editScheduleAlarm(AlarmInfo(it.id, it.reminderDateTime!!.triggerDateTime()))
        }
    }

    /**
     * check reminders that schedule and not fired if any issue occurred set to fired if currentDateTime after reminded DateTime
     */
    private suspend fun checkIfRemindedNoteIsAfterCurrentDateTime(noteDao: NoteDao, notes: List<Note>) {
        val currentDateTime = LocalDateTime.now()
        notes.filter {
            currentDateTime > it.reminderDateTime
        }.forEach {
            withContext(Dispatchers.IO) {
                it.isReminded = true
                noteDao.updateNote(it)
            }
        }
    }

    fun getAllNotesFromRoom(): Flow<List<Note>> = noteDao.getAllNotesFlow()

    fun getNoteByIdFromRoom(noteId: Int): Flow<Note?> = noteDao.getNoteByIdFlow(noteId)

    suspend fun insertNoteToRoom(note: Note): Long {
        val getId = noteDao.insertNote(note)
        if (note.reminderDateTime != null && !note.isReminded) {
            alarmScheduler.scheduleAlarm(AlarmInfo(getId.toInt(), note.reminderDateTime!!.triggerDateTime()))
        }
        return getId
    }

    suspend fun insertListOfNotesToRoom(notes: List<Note>): List<Long> = noteDao.insertListOfNotes(notes)
    suspend fun getNoteById(noteId: Int): Note? = noteDao.getNoteById(noteId)

    suspend fun updateNoteInRoom(note: Note, cancelAlarm: Boolean = false) {
        val getOldNote = getNoteById(note.id)
        noteDao.updateNote(note)
        if (note.reminderDateTime != null && !note.isReminded &&
            getOldNote != null && getOldNote.reminderDateTime != note.reminderDateTime && !cancelAlarm
        ) {
            alarmScheduler.editScheduleAlarm(AlarmInfo(note.id, note.reminderDateTime!!.triggerDateTime()))
        }
        if (cancelAlarm) {
            alarmScheduler.cancelAlarm(AlarmInfo(note.id, 0))
        }
    }

    suspend fun deleteNoteFromRoom(note: Note) = noteDao.deleteNote(note)

    suspend fun deleteNotesFromRoom(noteList: List<Note>) = noteDao.deleteListOfNote(noteList)
}
