package com.aritra.notify.ui.screens.notes.editNoteScreen

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.aritra.notify.data.models.Note
import com.aritra.notify.data.repository.NoteRepository
import com.aritra.notify.domain.usecase.SaveSelectedImageUseCase
import com.aritra.notify.utils.toFile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class EditScreenViewModel @Inject constructor(
    application: Application,
    private val editScreenRepository: NoteRepository
) : AndroidViewModel(application) {

    var noteModel = MutableLiveData(Note(0, "", "", Date(), null))
    var noteHasBeenModified = MutableLiveData(false)
    fun getNoteById(noteId: Int) = viewModelScope.launch(Dispatchers.IO) {
        editScreenRepository.getNoteByIdFromRoom(noteId).collect { response ->
            noteModel.postValue(response)
        }
    }

    fun updateNotes(noteModel: Note) = viewModelScope.launch(Dispatchers.IO) {
        // retrieve the note from the database to check if the image has been modified
        val note = editScreenRepository.getNoteByIdFromRoom(noteModel.id).first()
        // if the image has been modified, delete the old image
        if (note.image != noteModel.image) {
            note.image?.let { image ->
                image.toFile(getApplication())?.delete()
            }
        }
        editScreenRepository.updateNoteInRoom(
            noteModel.copy(
                // if the image has not been modified, use the old image uri
                image = if (note.image == noteModel.image) {
                    note.image
                } else {
                    // if the image has been modified, save the new image uri
                    SaveSelectedImageUseCase(
                        getApplication(),
                        noteModel.image!!,
                        noteModel.id
                    )
                }
            )
        )
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