package com.aritra.notify.ui.screens.notes.addEditScreen

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ElevatedAssistChip
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.aritra.notify.R
import com.aritra.notify.components.appbar.AddEditBottomBar
import com.aritra.notify.components.appbar.AddEditTopBar
import com.aritra.notify.components.camPreview.CameraPreview
import com.aritra.notify.components.dialog.DateTimeDialog
import com.aritra.notify.components.dialog.TextDialog
import com.aritra.notify.components.drawing.DrawingScreen
import com.aritra.notify.domain.models.Note
import com.aritra.notify.domain.models.Todo
import com.aritra.notify.ui.screens.notes.addEditScreen.components.DescriptionTextField
import com.aritra.notify.ui.screens.notes.addEditScreen.components.NoteChecklist
import com.aritra.notify.ui.screens.notes.addEditScreen.components.NoteImages
import com.aritra.notify.ui.screens.notes.addEditScreen.components.NoteStats
import com.aritra.notify.ui.theme.NotifyTheme
import com.aritra.notify.utils.formatReminderDateTime
import java.time.LocalDateTime
import java.util.Date

@Composable
fun AddEditScreen(
    note: Note,
    isNew: Boolean,
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
    saveNote: (String, String, List<Uri>, List<Todo>) -> Unit,
    deleteNote: (() -> Unit) -> Unit,
    onUpdateReminderDateTime: (LocalDateTime?) -> Unit,
) {
    val focus = LocalFocusManager.current
    var title by remember { mutableStateOf(note.title) }
    var description by remember { mutableStateOf(note.note) }
    var showAddTodo by remember { mutableStateOf(false) }
    val images = remember { mutableStateListOf<Uri>() }
    val checklist = remember { mutableStateListOf<Todo>() }
    val cancelDialogState = remember { mutableStateOf(false) }
    var openCameraPreview by remember { mutableStateOf(false) }
    var isEditDateTime by remember { mutableStateOf(false) }
    var openDrawingScreen by remember { mutableStateOf(false) }
    var shouldShowDialogDateTime by remember { mutableStateOf(false) }

    // Makes sure that the title is updated when the note is updated
    LaunchedEffect(note.title) {
        title = note.title
    }
    LaunchedEffect(note.note) {
        description = note.note
    }
    LaunchedEffect(note.image) {
        images.clear()
        images.addAll(note.image.filterNotNull())
    }
    LaunchedEffect(note.checklist) {
        checklist.clear()
        checklist.addAll(note.checklist.sortedBy { it.isChecked })
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
                    saveNote(title, description, images, checklist)
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
                                    if (index in images.indices) {
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

                    note.reminderDateTime?.let {
                        ElevatedAssistChip(leadingIcon = {
                            Icon(imageVector = Icons.Default.AccessTime, contentDescription = "")
                        }, onClick = {
                            isEditDateTime = !isEditDateTime
                        }, label = {
                            Text(
                                text = it.formatReminderDateTime(),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                textDecoration = if (note.isReminded) TextDecoration.LineThrough else null
                            )
                        }, trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "",
                                modifier = Modifier.clickable {
                                    onUpdateReminderDateTime(null)
                                }
                            )
                        }, modifier = Modifier)
                    }

                    NoteChecklist(
                        checklist = checklist,
                        showAddTodo = showAddTodo,
                        onAdd = {
                            checklist.add(Todo(title = it))
                        },
                        onDelete = {
                            checklist.removeAt(it)
                        },
                        onUpdate = { index, newTitle ->
                            checklist[index] = checklist[index].copy(title = newTitle)
                        },
                        onToggle = { index ->
                            checklist[index] = checklist[index].copy(isChecked = !checklist[index].isChecked)
                            checklist.sortBy { it.isChecked }
                        },
                        hideAddTodo = {
                            showAddTodo = false
                        }
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
                    showDrawingScreen = {
                        openDrawingScreen = true
                    },
                    showCameraSheet = {
                        openCameraPreview = true
                    },
                    onImagesSelected = {
                        images += it
                    },
                    onSpeechRecognized = {
                        description += " $it"
                    },
                    onReminderDateTime = {
                        shouldShowDialogDateTime = true
                    },
                    addTodo = {
                        showAddTodo = true
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

    if (openDrawingScreen) {
        DrawingScreen(
            onBack = {
                openDrawingScreen = false
            },
            onSave = {
                images += it
                openDrawingScreen = false
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

    DateTimeDialog(
        isOpen = shouldShowDialogDateTime, isEdit = isEditDateTime,
        onDateTimeUpdated = {
            onUpdateReminderDateTime(it)
            shouldShowDialogDateTime = false
        },
    ) {
        shouldShowDialogDateTime = false
        isEditDateTime = false
    }
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
        saveNote = { _, _, _, _ -> },
        deleteNote = {},
        onUpdateReminderDateTime = {}
    )
}
