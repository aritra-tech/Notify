package com.aritra.notify.components.actions

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.aritra.notify.R
import com.aritra.notify.components.dialog.TextDialog
import com.aritra.notify.components.note.NotesCard
import com.aritra.notify.data.models.Note
import com.aritra.notify.ui.screens.notes.homeScreen.NoteScreenViewModel
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox

@Composable
fun SwipeDelete(
    notesModel: Note,
    viewModel: NoteScreenViewModel,
    navigateToUpdateNoteScreen: (noteId: Int) -> Unit
) {

    val deleteDialogVisible = remember { mutableStateOf(false) }

    val delete = SwipeAction(
        onSwipe = {
            deleteDialogVisible.value = true
        },
        icon = {
            Icon(
                modifier = Modifier.padding(12.dp),
                painter = painterResource(R.drawable.ic_delete),
                contentDescription = null,
                tint = Color.White
            )
        },
        background = Color.Red,
    )

    SwipeableActionsBox(
        modifier = Modifier.padding(10.dp),
        swipeThreshold = 100.dp,
        endActions = listOf(delete)
    ) {
        NotesCard(notesModel, navigateToUpdateNoteScreen)
    }
    if (deleteDialogVisible.value) {
        TextDialog(
            title = stringResource(R.string.warning),
            description = stringResource(R.string.are_you_sure_want_to_delete_these_items_it_cannot_be_recovered),
            isOpened = deleteDialogVisible.value,
            onDismissCallback = { deleteDialogVisible.value = false },
            onConfirmCallback = {
                viewModel.deleteNote(notesModel)
                deleteDialogVisible.value = false
            }
        )
    }
}
