package com.aritra.notify.ui.screens.notes.homeScreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.aritra.notify.domain.models.Note
import com.aritra.notify.domain.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteScreenViewModel @Inject constructor(
    application: Application,
    private val homeRepository: NoteRepository
) : AndroidViewModel(application) {

    var listOfNotes = homeRepository.getAllNotesFromRoom().asLiveData()

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            homeRepository.deleteNoteFromRoom(note)
        }
    }

}