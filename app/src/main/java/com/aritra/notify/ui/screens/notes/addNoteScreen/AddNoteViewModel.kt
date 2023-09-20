package com.aritra.notify.ui.screens.notes.addNoteScreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.aritra.notify.data.models.Note
import com.aritra.notify.data.repository.NoteRepository
import com.aritra.notify.domain.usecase.SaveSelectedImageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AddNoteViewModel @Inject constructor(
    application: Application,
    private val addRepository: NoteRepository
) : AndroidViewModel(application) {

    fun insertNote(note: Note, onSuccess: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val id: Int = addRepository.insertNoteToRoom(note).toInt()
            if (note.image == null) {
                return@launch
            }

            // update the note with the new image uri
            addRepository.insertNoteToRoom(
                note.copy(
                    id = id,
                    image = SaveSelectedImageUseCase(
                        context = getApplication(),
                        uri = note.image!!,
                        noteId = id
                    )
                )
            )

            withContext(Dispatchers.Main) {
                onSuccess()
            }
        }
    }
}
