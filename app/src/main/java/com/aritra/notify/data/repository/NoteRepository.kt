package com.aritra.notify.data.repository

import android.app.Application
import com.aritra.notify.data.dao.NoteDao
import com.aritra.notify.data.db.NoteDatabase
import com.aritra.notify.data.models.Note
import kotlinx.coroutines.flow.Flow

class NoteRepository(application: Application) {

    private var noteDao: NoteDao

    init {
        val database = NoteDatabase.getInstance(application)
        noteDao = database.noteDao()
    }

    fun getAllNotesFromRoom(): Flow<List<Note>> = noteDao.getAllNotes()

    fun getNoteByIdFromRoom(noteId: Int): Flow<Note> = noteDao.getNoteById(noteId)

    fun searchNotesByTitleFromRoom(searchQuery: String): Flow<List<Note>> =
        noteDao.searchNotesByTitle(searchQuery)

    suspend fun insertNoteToRoom(note: Note) = noteDao.insertNote(note)

    suspend fun updateNoteInRoom(note: Note) = noteDao.updateNote(note)

    suspend fun deleteNoteFromRoom(note: Note) = noteDao.deleteNote(note)
}