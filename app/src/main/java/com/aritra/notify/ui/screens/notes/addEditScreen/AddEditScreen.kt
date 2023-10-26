package com.aritra.notify.ui.screens.notes.addEditScreen

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.aritra.notify.R
import com.aritra.notify.components.appbar.AddEditBottomBar
import com.aritra.notify.components.appbar.AddEditTopBar
import com.aritra.notify.components.camPreview.CameraPreview
import com.aritra.notify.components.dialog.TextDialog
import com.aritra.notify.domain.models.Note
import com.aritra.notify.ui.theme.NotifyTheme
import java.util.Date

@Composable
fun AddEditScreen(
    note: Note,
    isNew: Boolean,
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
    showDrawingScreen: () -> Unit,
    saveNote: (String, String, List<Uri>) -> Unit,
    deleteNote: (() -> Unit) -> Unit,
) {
    val focus = LocalFocusManager.current

    var title by remember {
        mutableStateOf(note.title)
    }
    var description by remember {
        mutableStateOf(note.note)
    }
    val images = remember {
        mutableStateListOf(*note.image.filterNotNull().toTypedArray())
    }
    val cancelDialogState = remember {
        mutableStateOf(false)
    }
    var openCameraPreview by remember {
        mutableStateOf(false)
    }

    // Makes sure that the title is updated when the note is updated
    LaunchedEffect(note.title) {
        title = note.title
    }

    // Makes sure that the description is updated when the note is updated
    LaunchedEffect(note.note) {
        description = note.note
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            AddEditTopBar(
                title = title,
                description = description,
                isNew = isNew,
                onBackPress = if (isNew) {
                    { cancelDialogState.value = true }
                } else {
                    navigateBack
                },
                saveNote = {
                    Log.e("AddEditScreen app bar", title)
                    Log.e("AddEditScreen app bar", description)
                    saveNote(title, description, images)
                },
                deleteNote = deleteNote
            )
        },
        content = { scaffoldPadding ->
            val scrollState = rememberScrollState()
            var descriptionScrollOffset by remember { mutableIntStateOf(0) }
            var contentSize by remember { mutableIntStateOf(0) }

            Column(
                modifier = Modifier
                    .padding(scaffoldPadding)
                    .onGloballyPositioned {
                        contentSize = it.size.height
                    }
                    .fillMaxSize()
                    .verticalScroll(scrollState),
                content = {
                    Column(
                        modifier = Modifier.onGloballyPositioned { layoutCoordinates ->
                            descriptionScrollOffset = layoutCoordinates.size.height
                        },
                        content = {
                            NoteImages(
                                images = images,
                                isNew = isNew,
                                onRemoveImage = { index ->
                                    if (index >= 0 && index < images.lastIndex) {
                                        images.removeAt(index)
                                    }
                                }
                            )

                            TextField(
                                modifier = Modifier.fillMaxWidth(),
                                value = title,
                                onValueChange = { newTitle ->
                                    title = newTitle
                                },
                                placeholder = {
                                    Text(
                                        stringResource(R.string.title),
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.W700,
                                        color = Color.Gray,
                                        fontFamily = FontFamily(Font(R.font.poppins_medium))
                                    )
                                },
                                textStyle = TextStyle(
                                    fontSize = 24.sp,
                                    fontFamily = FontFamily(Font(R.font.poppins_medium))
                                ),
                                maxLines = Int.MAX_VALUE,
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                                    disabledContainerColor = MaterialTheme.colorScheme.surface,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    disabledIndicatorColor = Color.Transparent
                                ),
                                keyboardOptions = KeyboardOptions.Default.copy(
                                    capitalization = KeyboardCapitalization.Sentences,
                                    keyboardType = KeyboardType.Text,
                                    imeAction = ImeAction.Next
                                ),
                                keyboardActions = KeyboardActions(
                                    onNext = {
                                        focus.moveFocus(FocusDirection.Down)
                                    }
                                )
                            )
                        }
                    )

                    NoteStats(
                        title = title,
                        description = description,
                        dateTime = note.dateTime
                    )

                    DescriptionTextField(
                        scrollOffset = descriptionScrollOffset,
                        contentSize = contentSize,
                        description = description,
                        parentScrollState = scrollState,
                        isNewNote = isNew,
                        onDescriptionChange = { newDescription ->
                            description = newDescription
                        }
                    )
                }
            )
        },
        bottomBar = {
            if (isNew) {
                AddEditBottomBar(
                    showDrawingScreen = showDrawingScreen,
                    showCameraSheet = {
                        openCameraPreview = true
                    },
                    onImagesSelected = {
                        images += it
                    },
                    onSpeechRecognized = {
                        description += " $it"
                    }
                )
            }
        }
    )

    if (openCameraPreview) {
        CameraPreview(
            close = {
                openCameraPreview = false
            },
            onImageCaptured = { image ->
                images += image
            }
        )
    }

    TextDialog(
        title = stringResource(R.string.are_you_sure),
        description = stringResource(R.string.the_text_change_will_not_be_saved),
        isOpened = cancelDialogState.value,
        onDismissCallback = { cancelDialogState.value = false },
        onConfirmCallback = {
            navigateBack()
            cancelDialogState.value = false
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun AddEditScreenPreview() = NotifyTheme {
    AddEditScreen(
        note = Note(
            title = "Title",
            note = "Description",
            image = listOf(),
            dateTime = Date()
        ),
        isNew = true,
        navigateBack = {},
        showDrawingScreen = {},
        saveNote = { _, _, _ -> },
        deleteNote = {}
    )
}
