package com.aritra.notify.ui.screens.notes.trash

import TrashNoteState
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.aritra.notify.components.note.NotesCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrashNoteScreen(
    trashNoteState: TrashNoteState,
    onEvent: (TrashNoteEvent) -> Unit,
) {
    Scaffold(topBar = {
        TopAppBar(title = {
            Text(
                text = if (trashNoteState.isSelectionMode) {
                    "${trashNoteState.selectedIds.size} item selected"
                } else {
                    "Trash Note"
                }
            )
        }, modifier = Modifier.shadow(2.dp))
    }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                items(trashNoteState.trashNotes) {
                    NotesCard(
                        noteModel = it.note,
                        isSelected = trashNoteState.selectedIds.contains(
                            it.note.id
                        ),
                        dateTimeDeleted = it,
                        onClick = {
                            if (trashNoteState.isSelectionMode) {
                                onEvent(TrashNoteEvent.AddSelectedIds(it.note.id))
                            }
                        }
                    ) {
                        if (!trashNoteState.isSelectionMode) {
                            onEvent(TrashNoteEvent.UpdateSelectionMode)
                            onEvent(TrashNoteEvent.AddSelectedIds(it.note.id))
                        }
                    }
                }
            }

            AnimatedVisibility(visible = trashNoteState.isSelectionMode) {
                DeleteAndRestoreSection(onTrashEvent = onEvent)
            }
        }
    }
}

@Composable
fun DeleteAndRestoreSection(
    modifier: Modifier = Modifier,
    onTrashEvent: (TrashNoteEvent) -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextButton(onClick = {
            onTrashEvent(TrashNoteEvent.RestoreNote)
        }) {
            Text(text = "Restore")
        }
        TextButton(
            onClick = {
                onTrashEvent(TrashNoteEvent.DeleteNote)
            },
            colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
        ) {
            Text(text = "Delete")
        }
    }
}
