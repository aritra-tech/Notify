package com.aritra.notify.ui.screens.trash_note

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aritra.notify.core.dispatcher.DispatcherProvider
import com.aritra.notify.domain.models.TrashNote
import com.aritra.notify.domain.repository.NoteRepository
import com.aritra.notify.domain.repository.trash.TrashNoteRepository
import com.aritra.notify.domain.repository.trash.TrashRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrashNoteViewModel @Inject constructor(
    private val trashNoteRepository: TrashRepository,
    private val dispatcherProvider: DispatcherProvider,
    private val noteRepository: NoteRepository
):ViewModel() {

    private val _state = MutableStateFlow(TrashNoteState())
    val state = _state.asStateFlow()

    private val _effect = MutableStateFlow<TrashNoteEffect?>(null)
    val effect = _effect.asStateFlow()
    init {
        viewModelScope.launch(dispatcherProvider.io) {
            val getNotes = trashNoteRepository.getTrashNoteWithNote().map { it.note }
            _state.update {
                it.copy(
                    getNotes
                )
            }
        }
    }

    fun onEvent(event:TrashNoteEvent){
        when(event){
            is TrashNoteEvent.AddSelectedIds -> {
                val trashNoteState = _state.value
                val getSelectedIds = trashNoteState.selectedIds
                if (!trashNoteState.selectedIds.contains(event.id)){
                    _state.update {
                        it.copy(
                            selectedIds = getSelectedIds + event.id
                        )
                    }
                }else{
                    val updateSelectedIds = getSelectedIds - event.id
                    _state.update {
                        it.copy(
                            selectedIds = updateSelectedIds,
                            isSelectionMode = updateSelectedIds.isNotEmpty()
                        )
                    }

                }
            }
            TrashNoteEvent.DeleteNote -> {
                viewModelScope.launch(dispatcherProvider.io) {
                    val trashNote = _state.value
                    if (trashNote.selectedIds.isNotEmpty()){
                        trashNote.selectedIds.forEach {
                            noteRepository.deleteNoteById(it)
                            trashNoteRepository.deleteTrashNoteById(it)
                            _effect.update {
                                TrashNoteEffect.Message("Deleted successfully")
                            }

                        }
                    }
                }
            }
            TrashNoteEvent.RestoreNote -> {
                viewModelScope.launch(dispatcherProvider.io) {
                    val trashNote = _state.value
                    if (trashNote.selectedIds.isNotEmpty()){
                        trashNote.selectedIds.forEach {
                            val getNote = noteRepository.getNoteById(it)
                            noteRepository.updateNoteInRoom(getNote!!.copy(isMovedToTrash = false))
                            trashNoteRepository.deleteTrashNoteById(it)
                            _effect.update {
                                TrashNoteEffect.Message("Restore successfully")
                            }

                        }
                    }
                }
            }
            TrashNoteEvent.UpdateSelectionMode -> {
                _state.update {
                    it.copy(
                        isSelectionMode = !it.isSelectionMode
                    )
                }
            }
        }
    }

    fun resetEffect(){
        _effect.update {
            null
        }
    }

    fun closePage(){
        _effect.update {
            TrashNoteEffect.Close
        }
    }
}

sealed class TrashNoteEffect{
    object Close:TrashNoteEffect()
    class Message(val message:String):TrashNoteEffect()
}
sealed class TrashNoteEvent{
    object RestoreNote:TrashNoteEvent()
    object DeleteNote:TrashNoteEvent()
    object UpdateSelectionMode:TrashNoteEvent()
    class AddSelectedIds(val id:Int):TrashNoteEvent()

}