package com.aritra.notify.ui.screens.notes.homeScreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.aritra.notify.core.dispatcher.DispatcherProvider
import com.aritra.notify.domain.models.Note
import com.aritra.notify.domain.models.TrashNote
import com.aritra.notify.domain.repository.NoteRepository
import com.aritra.notify.domain.repository.trash.TrashNoteRepository
import com.aritra.notify.domain.repository.trash.TrashRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class NoteScreenViewModel @Inject constructor(
    application: Application,
    private val homeRepository: NoteRepository,
    private val trashNote: TrashRepository,
    private val dispatcherProvider: DispatcherProvider
) : AndroidViewModel(application) {

    var listOfNotes = homeRepository.getAllNotesFromRoom().asLiveData().map { it.filter { !it.isMovedToTrash } }

    fun deleteNote(note: Note) {
        viewModelScope.launch(dispatcherProvider.io) {
            moveToTrash(note)
            homeRepository.updateNoteInRoom(note.copy(isMovedToTrash = true))
        }
    }

    private suspend fun moveToTrash(note: Note){
        trashNote.upsertTrashNote(TrashNote(note.id, LocalDateTime.now()))
    }
    fun deleteListOfNote(noteList: List<Note>) {
        viewModelScope.launch(dispatcherProvider.io) {
            noteList.forEach {
                moveToTrash(it)
                homeRepository.updateNoteInRoom(it.copy(isMovedToTrash = true))
            }
        }
    }
}
