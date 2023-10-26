package com.aritra.notify.ui.screens.notes.addEditScreen

import android.net.Uri
import android.util.Log
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
import com.aritra.notify.navigation.NotifyScreens
import com.aritra.notify.ui.screens.notes.homeScreen.NoteScreenViewModel

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
    val drawing = backStack.savedStateHandle.get<Uri?>("drawing")

    val note by viewModel.note.collectAsState()

    val isNew = remember(noteId) {
        noteId == null
    }
    val navigateBack: () -> Unit = remember {
        {
            navController.popBackStack()
        }
    }
    val saveNote: (String, String, List<Uri>) -> Unit = remember(note, isNew) {
        { title, description, images ->
            Log.e("AddEditRoute", title)
            Log.e("AddEditRoute", description)
            if (isNew) {
                viewModel.insertNote(
                    title = title,
                    description = description,
                    images = images,
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
                    onSuccess = { updated ->
                        if (updated) {
                            navigateBack()
                            Toast.makeText(context, "Successfully Updated!", Toast.LENGTH_SHORT).show()
                        } else {
                            navigateBack()
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

    LaunchedEffect(drawing) {
        if (drawing != null) {
            viewModel.addImages(drawing)
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
        showDrawingScreen = { navController.navigate(NotifyScreens.Drawing.name) },
        saveNote = saveNote,
        deleteNote = deleteNote
    )
}
