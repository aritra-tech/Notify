package com.aritra.notify.ui.screens.notes.addEditScreen

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.aritra.notify.domain.models.Note
import com.aritra.notify.domain.repository.NoteRepository
import com.aritra.notify.domain.usecase.SaveSelectedImageUseCase
import com.aritra.notify.utils.toFile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class AddEditViewModel @Inject constructor(
    application: Application,
    private val noteRepository: NoteRepository,
) : AndroidViewModel(application) {

    private val _noteModel = MutableLiveData(Note(0, "", "", Date(), emptyList()))
    val noteModel: LiveData<Note> get() = _noteModel

    private val _bitmap = MutableStateFlow<List<Bitmap>>(emptyList())
    val bitmapList = _bitmap.asStateFlow()

    fun onPhotoTaken(bitmap: Bitmap) {
        _bitmap.value += bitmap
    }

    fun insertNote(note: Note, onSuccess: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val id: Int = noteRepository.insertNoteToRoom(note).toInt()

            val imageUris = note.image.filterNotNull()

            if (imageUris.isNotEmpty()) {
                // update the note with the new image uri
                noteRepository.updateNoteInRoom(
                    note.copy(
                        id = id,
                        image = SaveSelectedImageUseCase(
                            context = getApplication(),
                            uris = imageUris,
                            noteId = id
                        )
                    )
                )
            }

            withContext(Dispatchers.Main) {
                onSuccess()
            }
        }
    }

    fun insertListOfNote(notes: List<Note>, onSuccess: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.insertListOfNotesToRoom(notes)

            notes.forEach { note ->
                val imageUris = note.image.filterNotNull()
                if (imageUris.isNotEmpty()) {
                    // update the note with the new image uri
                    noteRepository.updateNoteInRoom(
                        note.copy(
                            id = note.id,
                            image = SaveSelectedImageUseCase(
                                context = getApplication(),
                                uris = imageUris,
                                noteId = note.id
                            )
                        )
                    )
                }
            }

            withContext(Dispatchers.Main) {
                onSuccess()
            }
        }
    }

    fun getNoteById(noteId: Int) = viewModelScope.launch(Dispatchers.IO) {
        noteRepository.getNoteByIdFromRoom(noteId).collect { response ->
            _noteModel.postValue(response)
        }
    }
    /*fun getNoteById(noteId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val note = if (noteId != 0) {
                noteRepository.getNoteByIdFromRoom(noteId).first()
            } else {
                // If noteId is 0, it's a new note
                Note(0, "", "", Date(), null)
            }
            _noteModel.postValue(note)
        }
    }*/

    fun updateNotes(onSuccess: (updated: Boolean) -> Unit) = viewModelScope.launch(Dispatchers.IO) {
        val newNote = noteModel.value ?: return@launch
        // retrieve the note from the database to check if the image has been modified
        val oldNote = noteRepository.getNoteByIdFromRoom(newNote.id).first()
        // exit the method if the note has not been modified

        if (oldNote.title == newNote.title && oldNote.note == newNote.note && oldNote.image == newNote.image) {
            // Note has not been modified
            withContext(Dispatchers.Main) {
                onSuccess(false)
            }
            return@launch
        }
//        if (oldNote.title == newNote.title && oldNote.note == newNote.note && oldNote.image == newNote.image) return@launch
        // if the image has been modified, delete the old image
        if (oldNote.image != newNote.image) {
            oldNote.image.forEach { imageUri ->
                imageUri?.toFile(getApplication())?.delete()
            }
        }
        noteRepository.updateNoteInRoom(
            newNote.copy(
                // if the image has not been modified, use the old image uri
                image = if (oldNote.image == newNote.image) {
                    oldNote.image
                } else if (newNote.image.filterNotNull().isNotEmpty()) {
                    // if the image has been modified, save the new image uri
                    SaveSelectedImageUseCase(
                        getApplication(),
                        newNote.image.filterNotNull(),
                        newNote.id
                    )
                } else {
                    emptyList()
                }
            )
        )

        withContext(Dispatchers.Main) {
            onSuccess(true)
        }
    }

    fun updateTitle(title: String) {
        _noteModel.postValue(noteModel.value?.copy(title = title))
    }

    fun updateDescription(description: String) {
        _noteModel.postValue(noteModel.value?.copy(note = description))
    }

    /*   fun updateImage(imageList: List<Uri?>) {
           _noteModel.postValue(noteModel.value?.copy(image = imageList))
       }*/
}
