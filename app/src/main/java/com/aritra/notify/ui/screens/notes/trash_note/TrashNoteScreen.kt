package com.aritra.notify.ui.screens.notes.trash_note


import TrashNoteState
import android.widget.Toast
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.aritra.notify.components.note.NotesCard
import com.aritra.notify.navigation.NotifyScreens

fun NavGraphBuilder.trashNoteDest(controller: NavController) {
    composable(NotifyScreens.TrashNoteScreen.name) {
        val trashViewModel = hiltViewModel<TrashNoteViewModel>()
        val state by trashViewModel.state.collectAsState()
        val effect by trashViewModel.effect.collectAsState()
        val context = LocalContext.current
        LaunchedEffect(key1 = effect) {
            effect?.let {
                when (it) {
                    TrashNoteEffect.Close -> {
                        controller.popBackStack()
                        trashViewModel.resetEffect()
                    }

                    is TrashNoteEffect.Message -> {
                        Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                        trashViewModel.closePage()
                    }

                    else -> {}
                }
            }
        }
        TrashNoteScreen(trashNoteState = state, trashViewModel::onEvent)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrashNoteScreen(
    trashNoteState: TrashNoteState,
    onEvent: (TrashNoteEvent) -> Unit
) {

    Scaffold(topBar = {
        TopAppBar(title = {
            Text(text = if (trashNoteState.isSelectionMode) "${trashNoteState.selectedIds.size} item selected" else "Trash Note")
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
                    NotesCard(noteModel = it, isSelected = trashNoteState.selectedIds.contains(it.id), onClick = {
                        if (trashNoteState.isSelectionMode) {
                            onEvent(TrashNoteEvent.AddSelectedIds(it.id))
                        }
                    }) {
                        if (!trashNoteState.isSelectionMode) {
                            onEvent(TrashNoteEvent.UpdateSelectionMode)
                            onEvent(TrashNoteEvent.AddSelectedIds(it.id))
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
    onTrashEvent: (TrashNoteEvent) -> Unit
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