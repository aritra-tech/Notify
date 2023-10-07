package com.aritra.notify.ui.screens.notes.addEditScreen

import android.Manifest
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aritra.notify.R
import com.aritra.notify.components.actions.BottomSheetOptions
import com.aritra.notify.components.actions.SpeechRecognizerContract
import com.aritra.notify.components.dialog.TextDialog
import com.aritra.notify.components.topbar.AddEditTopBar
import com.aritra.notify.domain.models.Note
import com.aritra.notify.utils.Const
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import me.saket.telephoto.zoomable.coil.ZoomableAsyncImage
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun AddEditScreen(
    noteId: Int = 0,
    navigateBack: () -> Unit,
) {
    val addEditViewModel = hiltViewModel<AddEditViewModel>()
    val context = LocalContext.current
    val isNew = noteId == 0

    val note = if (isNew) {
        null
    } else {
        Note(noteId, "", "", Date(), emptyList())
    }

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var dateTime by remember { mutableStateOf(Calendar.getInstance().time) }
    var photoUri by remember { mutableStateOf(emptyList<Uri?>()) }

    var characterCount by remember { mutableIntStateOf(title.length + description.length) }
    val cancelDialogState = remember { mutableStateOf(false) }
    var showSheet by remember { mutableStateOf(false) }
    val dateFormat = SimpleDateFormat(Const.DATE_FORMAT, Locale.getDefault())
    val timeFormat = SimpleDateFormat(Const.TIME_FORMAT, Locale.getDefault())
    timeFormat.isLenient = false
    val currentDate = dateFormat.format(Calendar.getInstance().time)
    val currentTime = timeFormat.format(Calendar.getInstance().time).uppercase(Locale.getDefault())
    val focus = LocalFocusManager.current
    val skipPartiallyExpanded by remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = skipPartiallyExpanded
    )
    val formattedDateTime = SimpleDateFormat(Const.DATE_TIME_FORMAT, Locale.getDefault()).format(dateTime ?: 0)
    val formattedCharacterCount = "${(title.length) + (description.length)} characters"

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.PickMultipleVisualMedia()) { uris ->
        photoUri = uris
    }

    val permissionState = rememberPermissionState(
        permission = Manifest.permission.RECORD_AUDIO
    )

    // add note
    if (isNew) {
        SideEffect {
            permissionState.launchPermissionRequest()
        }
    }
    val speechRecognizerLauncher = rememberLauncherForActivityResult(contract = SpeechRecognizerContract(), onResult = {
        it?.let {
            for (st in it) {
                description += " $st"
            }
        }
    })

// edit note
    if (!isNew) {
        title = addEditViewModel.noteModel.observeAsState().value?.title ?: ""
        description = addEditViewModel.noteModel.observeAsState().value?.note ?: ""
        photoUri = addEditViewModel.noteModel.observeAsState().value?.image ?: emptyList()
        dateTime = addEditViewModel.noteModel.observeAsState().value?.dateTime

        LaunchedEffect(Unit) {
            addEditViewModel.getNoteById(noteId)
        }
    }

    val saveEditNote: () -> Unit = if (isNew) {
        remember {
            {
                addEditViewModel.insertNote(
                    note = Note(
                        id = 0,
                        title = title,
                        note = description,
                        dateTime = dateTime,
                        image = photoUri
                    ),
                    onSuccess = {
                        navigateBack()
                        Toast.makeText(context, "Successfully Saved!", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    } else {
        remember {
            {
                addEditViewModel.updateNotes { updated ->
                    if (updated) {
                        navigateBack()
                        Toast.makeText(context, "Successfully Updated!", Toast.LENGTH_SHORT).show()
                    } else {
                        navigateBack()
                    }
                }
            }
        }
    }

    Scaffold(topBar = {
        AddEditTopBar(
            title = title,
            description = description,
            onBackPress = if (isNew) {
                { cancelDialogState.value = true }
            } else {
                navigateBack
            },
            saveNote = if (isNew) {
                saveEditNote
            } else {
                {}
            },
            updateNote = if (isNew) {
                {}
            } else {
                saveEditNote
            },
            note = note
        )
    }, bottomBar = {
        if (isNew) {
            Column(
                Modifier
                    .navigationBarsPadding()
                    .imePadding()
            ) {
                BottomAppBar(content = {
                    IconButton(onClick = { showSheet = true }) {
                        Icon(
                            painter = painterResource(id = R.drawable.add_box_icon),
                            contentDescription = stringResource(R.string.add_box)
                        )
                    }
                    if (showSheet) {
                        ModalBottomSheet(
                            onDismissRequest = { showSheet = false },
                            sheetState = bottomSheetState,
                            dragHandle = { BottomSheetDefaults.DragHandle() }
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .navigationBarsPadding()
                                    .padding(16.dp)
                            ) {
                                BottomSheetOptions(
                                    text = stringResource(R.string.add_image),
                                    icon = painterResource(id = R.drawable.gallery_icon),
                                    onClick = {
                                        launcher.launch(
                                            PickVisualMediaRequest(
                                                mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
                                            )
                                        )
                                        showSheet = false
                                    }
                                )
                                BottomSheetOptions(
                                    text = stringResource(R.string.speech_to_text),
                                    icon = painterResource(id = R.drawable.mic_icon),
                                    onClick = {
                                        if (permissionState.status.isGranted) {
                                            speechRecognizerLauncher.launch(Unit)
                                        } else {
                                            permissionState.launchPermissionRequest()
                                        }
                                        showSheet = false
                                    }
                                )
                            }
                        }
                    }
                })
            }
        }
    }) { contentPadding ->

        val scrollState = rememberScrollState()
        var descriptionScrollOffset by remember { mutableIntStateOf(0) }
        var contentSize by remember { mutableIntStateOf(0) }

        Box(
            modifier = Modifier
                .padding(contentPadding)
                .onGloballyPositioned {
                    contentSize = it.size.height
                }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                Column(
                    modifier = Modifier.onGloballyPositioned { layoutCoordinates ->
                        descriptionScrollOffset = layoutCoordinates.size.height
                    }
                ) {
                    if (isNew) {
                        if (photoUri.isNotEmpty()) {
                            LazyRow {
                                items(photoUri.size) {
                                    Box(
                                        Modifier
                                            .height(180.dp)
                                            .width(180.dp)
                                            .padding(4.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                    ) {
                                        ZoomableAsyncImage(
                                            modifier = Modifier.fillMaxSize(),
                                            model = photoUri[it],
                                            contentDescription = stringResource(R.string.image),
                                            contentScale = ContentScale.Crop
                                        )
                                        FilledTonalIconButton(
                                            modifier = Modifier.align(Alignment.TopEnd),
                                            onClick = {
                                                photoUri = photoUri.filterIndexed { index, _ -> index != it }
                                            },
                                            colors = IconButtonDefaults.filledTonalIconButtonColors(
                                                containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(
                                                    alpha = 0.6f
                                                )
                                            )
                                        ) {
                                            Icon(
                                                imageVector = Icons.Outlined.Close,
                                                contentDescription = stringResource(R.string.clear_image)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        Row(
                            modifier = Modifier
                                .horizontalScroll(rememberScrollState())
                        ) {
                            photoUri.forEach { uri ->
                                ZoomableAsyncImage(
                                    modifier = Modifier
                                        .height(180.dp)
                                        .width(180.dp)
                                        .padding(4.dp)
                                        .clip(RoundedCornerShape(8.dp)),
                                    model = uri ?: "",
                                    contentDescription = stringResource(R.string.image),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    }

                    TextField(
                        modifier = Modifier.fillMaxWidth(), value = title, onValueChange = { newTitle ->
                            if (isNew) {
                                title = newTitle
                                characterCount = title.length + description.length
                            } else {
                                addEditViewModel.updateTitle(newTitle)
                            }
                        }, placeholder = {
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
                        keyboardActions = KeyboardActions(onNext = {
                            focus.moveFocus(FocusDirection.Down)
                        })
                    )

                    TextField(
                        value = if (isNew) {
                            "$currentDate, $currentTime   |  $characterCount characters"
                        } else {
                            "$formattedDateTime | $formattedCharacterCount"
                        },
                        onValueChange = { },
                        modifier = Modifier.fillMaxWidth(),
                        readOnly = true,
                        textStyle = TextStyle(
                            fontSize = 15.sp,
                            fontFamily = FontFamily(Font(R.font.poppins_light))
                        ),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                            disabledContainerColor = MaterialTheme.colorScheme.surface,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent
                        ),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Text
                        )
                    )
                }

                DescriptionTextField(
                    scrollOffset = descriptionScrollOffset,
                    contentSize = contentSize,
                    description = description,
                    parentScrollState = scrollState,
                    onDescriptionChange = { newDescription ->
                        if (isNew) {
                            description = newDescription
                            characterCount = title.length + description.length
                        } else {
                            addEditViewModel.updateDescription(newDescription)
                        }
                    }
                )
            }
        }
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
