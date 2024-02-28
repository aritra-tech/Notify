package com.aritra.notify.ui.screens.notes.addEditScreen.route

import android.net.Uri
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.aritra.notify.domain.models.Todo
import com.aritra.notify.ui.screens.notes.addEditScreen.AddEditScreen
import com.aritra.notify.ui.screens.notes.addEditScreen.AddEditViewModel
import com.aritra.notify.ui.screens.notes.homeScreen.NoteScreenViewModel
import java.time.LocalDateTime

@Composable
fun AddEditRoute(
    navController: NavController,
    backStack: NavBackStackEntry,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val noteId = (backStack.arguments?.getInt("noteId") ?: 0).let {
        if (it < 0) null else it
    }
    val viewModel = hiltViewModel<AddEditViewModel>()
    val noteViewModel = hiltViewModel<NoteScreenViewModel>()

    val note by viewModel.note.collectAsState()

    val isNew = remember(noteId) {
        noteId == null
    }
    val navigateBack: () -> Unit = remember {
        {
            navController.popBackStack()
        }
    }
    val saveNote: (String, String, List<Uri>, List<Todo>) -> Unit = remember(note, isNew) {
        { title, description, images, checklist ->
            if (isNew) {
                viewModel.insertNote(
                    title = title,
                    description = description,
                    images = images,
                    checklist = checklist,
                    onSuccess = {
                        navigateBack()
                        Toast.makeText(context, "Successfully Saved!", Toast.LENGTH_SHORT).show()
                    }
                )
            } else {
                viewModel.updateNote(
                    title = title,
                    description = description,
                    images = images,
                    checklist = checklist,
                    onSuccess = { updated ->
                        navigateBack()
                        if (updated) {
                            Toast.makeText(context, "Successfully Updated!", Toast.LENGTH_SHORT).show()
                        }
                    }
                )
            }
        }
    }
    val deleteNote: (() -> Unit) -> Unit = remember(noteId) {
        {
            if (noteId != null) {
                noteViewModel.deleteNote(
                    noteId = noteId,
                    onSuccess = it
                )
            }
        }
    }

    val rememberUpdateDateTime = remember {
        { localDateTime: LocalDateTime? ->
            viewModel.updateReminderDateTime(localDateTime)
        }
    }
    LaunchedEffect(noteId) {
        viewModel.getNoteById(noteId)
    }

    AddEditScreen(
        modifier = modifier,
        note = note,
        isNew = isNew,
        navigateBack = navigateBack,
        saveNote = saveNote,
        deleteNote = deleteNote,
        onUpdateReminderDateTime = rememberUpdateDateTime
    )
}
