package com.aritra.notify.ui.screens.notes.editNoteScreen

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.aritra.notify.domain.models.Note
import com.aritra.notify.domain.repository.NoteRepository
import com.aritra.notify.domain.usecase.SaveSelectedImageUseCase
import com.aritra.notify.utils.toFile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class EditScreenViewModel @Inject constructor(
    application: Application,
    private val editScreenRepository: NoteRepository
) : AndroidViewModel(application) {

    var noteModel = MutableLiveData(Note(0, "", "", Date(), null))
    fun getNoteById(noteId: Int) = viewModelScope.launch(Dispatchers.IO) {
        editScreenRepository.getNoteByIdFromRoom(noteId).collect { response ->
            noteModel.postValue(response)
        }
    }

    fun updateNotes(onSuccess: () -> Unit) = viewModelScope.launch(Dispatchers.IO) {
        val newNote = noteModel.value ?: return@launch
        // retrieve the note from the database to check if the image has been modified
        val oldNote = editScreenRepository.getNoteByIdFromRoom(newNote.id).first()
        // exit the method if the note has not been modified
        if (oldNote.title == newNote.title && oldNote.note == newNote.note && oldNote.image == newNote.image) return@launch
        // if the image has been modified, delete the old image
        if (oldNote.image != newNote.image) {
            oldNote.image?.toFile(getApplication())?.delete()
        }
        editScreenRepository.updateNoteInRoom(
            newNote.copy(
                // if the image has not been modified, use the old image uri
                image = if (oldNote.image == newNote.image) {
                    oldNote.image
                } else if (newNote.image != null) {
                    // if the image has been modified, save the new image uri
                    SaveSelectedImageUseCase(
                        getApplication(),
                        newNote.image!!,
                        newNote.id
                    )
                } else null
            )
        )

        withContext(Dispatchers.Main) {
            onSuccess()
        }
    }

    fun updateTitle(title: String) {
        noteModel.postValue(noteModel.value?.copy(title = title))
    }

    fun updateDescription(description: String) {
        noteModel.postValue(noteModel.value?.copy(note = description))
    }

    fun updateImage(image: Uri?) {
        noteModel.postValue(noteModel.value?.copy(image = image))
    }
}