package com.aritra.notify.screens.notes.editNoteScreen

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.aritra.notify.data.models.Note
import com.aritra.notify.data.repository.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date

class EditScreenViewModel(application: Application) : AndroidViewModel(application) {

    private val noteRepo = NoteRepository(application)
    var noteModel by mutableStateOf(Note(0, "", "",Date()))

    fun getNoteById(noteId: Int) = viewModelScope.launch(Dispatchers.IO) {
        noteRepo.getNoteByIdFromRoom(noteId).collect { response ->
            noteModel = response
        }
    }

    fun updateNotes(noteModel: Note) = viewModelScope.launch(Dispatchers.IO) {
        noteRepo.updateNoteInRoom(noteModel)
    }

    fun updateTitle(title: String) {
        noteModel = noteModel.copy(title = title)
    }

    fun updateDescription(description: String) {
        noteModel = noteModel.copy(note = description)
    }
    fun updateDateTime(dateTime: Date) {
        noteModel = noteModel.copy(dateTime = dateTime)
    }
}