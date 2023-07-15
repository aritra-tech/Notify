package com.aritra.notify.screens.notes.editNoteScreen

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.aritra.notify.data.models.Note
import com.aritra.notify.data.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class EditScreenViewModel @Inject constructor(
    application: Application,
    private val editScreenRepository: NoteRepository
) : AndroidViewModel(application) {


    var noteModel by mutableStateOf(Note(0, "", "",Date()))

    fun getNoteById(noteId: Int) = viewModelScope.launch(Dispatchers.IO) {
        editScreenRepository.getNoteByIdFromRoom(noteId).collect { response ->
            noteModel = response
        }
    }

    fun updateNotes(noteModel: Note) = viewModelScope.launch(Dispatchers.IO) {
        editScreenRepository.updateNoteInRoom(noteModel)
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