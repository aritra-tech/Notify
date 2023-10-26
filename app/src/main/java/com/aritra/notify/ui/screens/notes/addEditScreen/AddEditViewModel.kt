package com.aritra.notify.ui.screens.notes.addEditScreen

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.aritra.notify.domain.models.Note
import com.aritra.notify.domain.repository.NoteRepository
import com.aritra.notify.domain.usecase.SaveSelectedImageUseCase
import com.aritra.notify.utils.toFile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class AddEditViewModel @Inject constructor(
    application: Application,
    private val noteRepository: NoteRepository,
) : AndroidViewModel(application) {

    private val _note = MutableStateFlow(Note(dateTime = Date()))
    val note: StateFlow<Note> = _note

    fun insertNote(note: Note, onSuccess: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val id: Int = noteRepository.insertNoteToRoom(note).toInt()

            val images = _note.value.image.filterNotNull()

            if (images.isNotEmpty()) {
                // update the note with the new image uri
                noteRepository.updateNoteInRoom(
                    note.copy(
                        id = id,
                        image = SaveSelectedImageUseCase(
                            context = getApplication(),
                            uris = images,
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

    fun getNoteById(noteId: Int?) = viewModelScope.launch(Dispatchers.IO) {
        var note = note.value
        if (noteId != null) {
            note = noteRepository.getNoteById(noteId) ?: note
        }
        _note.update { note }
    }

    fun updateNotes(onSuccess: (updated: Boolean) -> Unit) = viewModelScope.launch(Dispatchers.IO) {
        val newNote = note.value
        // retrieve the note from the database to check if the image has been modified
        val oldNote = noteRepository.getNoteById(newNote.id) ?: return@launch
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
        _note.update {
            it.copy(title = title)
        }
    }

    fun updateDescription(description: String) {
        _note.update {
            it.copy(note = description)
        }
    }

    fun addImages(vararg image: Uri?) {
        _note.update {
            it.copy(image = it.image + image)
        }
    }

    fun removeImage(index: Int) {
        _note.update {
            it.copy(
                image = it.image.toMutableList().apply {
                    if (index in 0..<size) {
                        removeAt(index)
                    }
                }.toList()
            )
        }
    }
}
