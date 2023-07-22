package com.aritra.notify.ui.screens.notes.addNoteScreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.aritra.notify.data.models.Note
import com.aritra.notify.data.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddNoteViewModel @Inject constructor(
    application: Application,
    private val addRepository: NoteRepository
) : AndroidViewModel(application) {

    fun insertNote(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        addRepository.insertNoteToRoom(note)
    }
}
