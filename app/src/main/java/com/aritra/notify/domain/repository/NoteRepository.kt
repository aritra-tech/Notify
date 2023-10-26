package com.aritra.notify.domain.repository

import android.app.Application
import com.aritra.notify.core.alarm.AlarmInfo
import com.aritra.notify.core.alarm.AlarmScheduler
import com.aritra.notify.data.dao.NoteDao
import com.aritra.notify.data.db.NoteDatabase
import com.aritra.notify.domain.models.Note
import com.aritra.notify.utils.triggerDateTime
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NoteRepository @Inject constructor(application: Application, private val alarmScheduler: AlarmScheduler) {

    private var noteDao: NoteDao

    init {
        val database = NoteDatabase.getInstance(application)
        noteDao = database.noteDao()
        // TODO: add here to check alarms again
    }

    suspend fun deleteNoteById(noteId: Int) {
        noteDao.deleteNoteById(noteId)
    }

    fun getAllNotesFromRoom(): Flow<List<Note>> = noteDao.getAllNotes()

    fun getNoteByIdFromRoom(noteId: Int): Flow<Note?> = noteDao.getNoteByIdFlow(noteId)

    suspend fun insertNoteToRoom(note: Note): Long  {
        val getId = noteDao.insertNote(note)
        if (note.reminderDateTime != null){
            alarmScheduler.scheduleAlarm(AlarmInfo(note.id,note.reminderDateTime!!.triggerDateTime()))
        }
        return getId
    }

    suspend fun insertListOfNotesToRoom(notes: List<Note>): List<Long> = noteDao.insertListOfNotes(notes)
    fun getNoteById(noteId: Int): Note? = noteDao.getNoteById(noteId)

    suspend fun updateNoteInRoom(note: Note) = noteDao.updateNote(note)

    suspend fun deleteNoteFromRoom(note: Note) = noteDao.deleteNote(note)

    suspend fun deleteNotesFromRoom(noteList: List<Note>) = noteDao.deleteListOfNote(noteList)
}
