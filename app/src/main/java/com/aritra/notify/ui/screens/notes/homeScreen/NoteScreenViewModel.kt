package com.aritra.notify.ui.screens.notes.homeScreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
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

    private val _selectedNoteIds = MutableLiveData<Set<Int>>(emptySet())
    val selectedNoteIds get() = _selectedNoteIds

    fun deleteNote(note: Note) {
         viewModelScope.launch {
            homeRepository.deleteNoteFromRoom(note)
        }
    }

    fun deleteListOfNote(noteList: List<Note>) {
        viewModelScope.launch {
            homeRepository.deleteNotesFromRoom(noteList)
        }
    }

}