package com.aritra.notify.ui.screens.notes.addNoteScreen


import android.Manifest
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
import coil.compose.AsyncImage
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
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun AddNotesScreen(
    navigateBack: () -> Unit
) {
    val addViewModel = hiltViewModel<AddNoteViewModel>()
    val context = LocalContext.current
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    val dateTime by remember { mutableStateOf(Calendar.getInstance().time) }
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
    var photoUri: Uri? by remember { mutableStateOf(null) }
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            photoUri = uri
        }

    val permissionState = rememberPermissionState(
        permission = Manifest.permission.RECORD_AUDIO
    )
    SideEffect {
        permissionState.launchPermissionRequest()
    }

    val speechRecognizerLauncher = rememberLauncherForActivityResult(
        contract = SpeechRecognizerContract(),
        onResult = {
            it?.let {
                for (st in it) {
                    description += " $st"
                }
            }
        }
    )

    val saveNote = remember {
        {
            addViewModel.insertNote(
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

    Scaffold(
        topBar = {
            AddEditTopBar(
                title = title,
                description = description,
                onBackPress = { cancelDialogState.value = true },
                saveNote = saveNote,
                updateNote = {},
                note = null
            )
        },
        bottomBar = {
            Column(
                Modifier
                    .navigationBarsPadding()
                    .imePadding()
            ) {
                BottomAppBar(
                    content = {
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
                    }
                )
            }
        }
    ) {
        Box(
            modifier = Modifier.padding(it)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
            ) {
                photoUri?.let {
                    Log.d("URI", "Photo uri $photoUri")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        IconButton(
                            onClick = {
                                photoUri = null
                            },
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Close,
                                contentDescription = stringResource(R.string.clear_image)
                            )
                        }
                    }

                    AsyncImage(
                        model = photoUri,
                        contentDescription = stringResource(R.string.image),
                        modifier = Modifier.fillMaxWidth(),
                        contentScale = ContentScale.Crop
                    )
                }

                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = title,
                    onValueChange = { newTitle ->
                        title = newTitle
                        characterCount = title.length + description.length
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
                        fontFamily = FontFamily(Font(R.font.poppins_medium)),
                    ),
                    maxLines = Int.MAX_VALUE,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        disabledContainerColor = MaterialTheme.colorScheme.surface,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        capitalization = KeyboardCapitalization.Sentences,
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(onNext = {
                        focus.moveFocus(FocusDirection.Down)
                    }),
                )

                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = "$currentDate, $currentTime   |  $characterCount characters",
                    onValueChange = { },
                    textStyle = TextStyle(
                        fontSize = 15.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_light))
                    ),
                    readOnly = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        disabledContainerColor = MaterialTheme.colorScheme.surface,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Text,
                    ),
                )

                TextField(
                    modifier = Modifier.fillMaxSize(),
                    value = description,
                    onValueChange = { newDescription ->
                        description = newDescription
                        characterCount = title.length + description.length
                    },
                    placeholder = {
                        Text(
                            stringResource(R.string.notes),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.W500,
                            color = Color.Gray,
                            fontFamily = FontFamily(Font(R.font.poppins_light))
                        )
                    },
                    textStyle = TextStyle(
                        fontSize = 18.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_light)),
                    ),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        disabledContainerColor = MaterialTheme.colorScheme.surface,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        capitalization = KeyboardCapitalization.Sentences,
                        keyboardType = KeyboardType.Text,
                    ),
                    maxLines = Int.MAX_VALUE,
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