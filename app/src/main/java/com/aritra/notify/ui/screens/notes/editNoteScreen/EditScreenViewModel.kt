package com.aritra.notify.ui.screens.notes.editNoteScreen

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
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

    var noteModel = MutableLiveData(Note(0,"","",Date(),null))
    var noteHasBeenModified = MutableLiveData(false)
    fun getNoteById(noteId: Int) = viewModelScope.launch(Dispatchers.IO) {
        editScreenRepository.getNoteByIdFromRoom(noteId).collect { response ->
            noteModel.postValue(response)
        }
    }

    fun updateNotes(noteModel: Note) = viewModelScope.launch(Dispatchers.IO) {
        editScreenRepository.updateNoteInRoom(noteModel)
    }

    fun updateTitle(title: String) {
        noteModel.postValue(noteModel.value?.copy(title = title))
        if (noteHasBeenModified.value == false) noteHasBeenModified.value = true
    }

    fun updateDescription(description: String) {
        noteModel.postValue(noteModel.value?.copy(note = description))
        if (noteHasBeenModified.value == false) noteHasBeenModified.value = true
    }

    fun updateImage(image: Uri?) {
        noteModel.postValue(noteModel.value?.copy(image = image))
        if (noteHasBeenModified.value == false) noteHasBeenModified.value = true
    }
}