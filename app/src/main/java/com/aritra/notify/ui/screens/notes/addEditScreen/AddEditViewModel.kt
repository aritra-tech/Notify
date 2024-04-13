package com.aritra.notify.ui.screens.notes.addEditScreen

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.aritra.notify.domain.models.Note
import com.aritra.notify.domain.models.Todo
import com.aritra.notify.domain.repository.NoteRepository
import com.aritra.notify.domain.usecase.SaveSelectedImageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class AddEditViewModel @Inject constructor(
    application: Application,
    private val noteRepository: NoteRepository,
) : AndroidViewModel(application) {

    private var noteId: Int? = null

    private val _note = MutableStateFlow(Note(dateTime = Date()))
    val note: StateFlow<Note> = _note

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
        this@AddEditViewModel.noteId = noteId

        var note = note.value
        if (noteId != null) {
            note = noteRepository.getNoteById(noteId) ?: note
        }
        _note.update { note }
    }

    fun insertNote(
        title: String,
        description: String,
        images: List<Uri>,
        checklist: List<Todo>,
        onSuccess: () -> Unit,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val note = _note.value.copy(
                title = title,
                note = description,
                checklist = checklist
            )

            val id: Int = noteRepository.insertNoteToRoom(note).toInt()

            if (images.isNotEmpty()) {
                noteRepository.updateNoteInRoom(
                    note.copy(
                        id = id,
                        // update the note with the new image uri
                        image = SaveSelectedImageUseCase(
                            context = getApplication(),
                            uris = images,
                            noteId = id
                        ),
                        // update the note with the new date time
                        dateTime = Date()
                    )
                )
            }

            withContext(Dispatchers.Main) {
                onSuccess()
            }
        }
    }

    fun updateNote(
        title: String,
        description: String,
        images: List<Uri>,
        checklist: List<Todo>,
        onSuccess: (updated: Boolean) -> Unit,
    ) = viewModelScope.launch(Dispatchers.IO) {
        val newNote = note.value
        // retrieve the note from the database to check if the image has been modified
        val oldNote = noteRepository.getNoteById(newNote.id) ?: return@launch

        // exit the method if the note has not been modified
        if (
            oldNote.title == title &&
            oldNote.note == description &&
            oldNote.image == images &&
            checklist == oldNote.checklist &&
            oldNote.reminderDateTime == newNote.reminderDateTime
        ) {
            // Note has not been modified
            withContext(Dispatchers.Main) {
                onSuccess(false)
            }
            return@launch
        }

        noteRepository.updateNoteInRoom(
            newNote.copy(
                title = title,
                note = description,
                dateTime = Date(),
                checklist = checklist
            )
        )

        withContext(Dispatchers.Main) {
            onSuccess(true)
        }
    }

    fun updateReminderDateTime(dateTime: LocalDateTime?) {
        _note.update {
            it.copy(
                reminderDateTime = dateTime,
                isReminded = false
            )
        }
    }
}
