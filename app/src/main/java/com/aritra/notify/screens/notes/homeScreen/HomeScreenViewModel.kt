package com.aritra.notify.screens.notes.homeScreen

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.aritra.notify.data.models.Note
import com.aritra.notify.data.repository.NoteRepository
import kotlinx.coroutines.launch

class HomeScreenViewModel(application: Application) : AndroidViewModel(application) {

    private val noteRepository = NoteRepository(application)
    var notesModel by mutableStateOf(emptyList<Note>())

    fun getAllNotes() {
        viewModelScope.launch {
            noteRepository.getAllNotesFromRoom().collect { response ->
                notesModel = response
            }
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            noteRepository.deleteNoteFromRoom(note)
        }
    }

    fun searchNotesByTitle(searchQuery: String) {
        viewModelScope.launch {
            noteRepository.searchNotesByTitleFromRoom(searchQuery).collect { response ->
                notesModel = response
            }
        }
    }
}