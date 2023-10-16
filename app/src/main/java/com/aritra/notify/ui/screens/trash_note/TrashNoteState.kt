package com.aritra.notify.ui.screens.trash_note

import androidx.compose.runtime.Immutable
import com.aritra.notify.domain.models.Note
import com.aritra.notify.domain.models.TrashNote

@Immutable
data class TrashNoteState(
    val trashNotes:List<Note> = emptyList(),
    val isSelectionMode:Boolean = false,
    val selectedIds:Set<Int> = emptySet()
)
