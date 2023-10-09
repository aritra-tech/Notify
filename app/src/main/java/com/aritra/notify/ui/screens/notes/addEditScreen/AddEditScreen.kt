package com.aritra.notify.ui.screens.notes.addEditScreen

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture.OnImageCapturedCallback
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.BottomSheetScaffold
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
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import androidx.compose.ui.tooling.data.SourceContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aritra.notify.R
import com.aritra.notify.components.actions.BottomSheetOptions
import com.aritra.notify.components.actions.ClickedPhotosBottomSheet
import com.aritra.notify.components.actions.SpeechRecognizerContract
import com.aritra.notify.components.camPreview.CameraPreview
import com.aritra.notify.components.dialog.TextDialog
import com.aritra.notify.components.topbar.AddEditTopBar
import com.aritra.notify.domain.models.Note
import com.aritra.notify.utils.Const
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.launch
import me.saket.telephoto.zoomable.coil.ZoomableAsyncImage
import java.io.ByteArrayOutputStream
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
    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState()
    var openCameraBottomSheet by remember {
        mutableStateOf(false)
    }
    val formattedDateTime = SimpleDateFormat(Const.DATE_TIME_FORMAT, Locale.getDefault()).format(dateTime ?: 0)
    val formattedCharacterCount = "${(title.length) + (description.length)} characters"

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.PickMultipleVisualMedia()) { uris ->
        photoUri = uris
    }
    val controller = remember {
        LifecycleCameraController(context).apply {
            setEnabledUseCases(CameraController.IMAGE_CAPTURE)
        }
    }
    val bitmap_List by addEditViewModel.bitmapList.collectAsStateWithLifecycle()
    val permissionState = rememberPermissionState(
        permission = Manifest.permission.RECORD_AUDIO
    )
    val camPermissionState = rememberPermissionState(
        permission = Manifest.permission.CAMERA
    )


    // add note
    if (isNew) {
        SideEffect {
            permissionState.launchPermissionRequest()
        }
        if ((permissionState.status.isGranted || !permissionState.status.isGranted) && !camPermissionState.status.isGranted) {
            SideEffect {
                camPermissionState.launchPermissionRequest()
            }
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
                                    text = stringResource(R.string.take_image),
                                    icon = painterResource(id = R.drawable.camera_icon),
                                    onClick = {
                                        if (camPermissionState.status.isGranted) {
                                            openCameraBottomSheet = true
                                        } else {
                                            camPermissionState.launchPermissionRequest()
                                        }
                                        showSheet = false
                                    }
                                )
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
    }) {
        Box(
            modifier = Modifier.padding(it)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
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

                TextField(
                    modifier = Modifier.fillMaxSize(),
                    value = description,
                    onValueChange = { newDescription ->
                        if (isNew) {
                            description = newDescription
                            characterCount = title.length + description.length
                        } else {
                            addEditViewModel.updateDescription(newDescription)
                        }
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
                        capitalization = KeyboardCapitalization.Sentences,
                        keyboardType = KeyboardType.Text
                    ),
                    maxLines = Int.MAX_VALUE

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

    if (openCameraBottomSheet) {
        BottomSheetScaffold(scaffoldState = scaffoldState, sheetPeekHeight = 0.dp, sheetContent = {
            ClickedPhotosBottomSheet(bitmaps = bitmap_List, modifier = Modifier.fillMaxWidth())
        }) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                CameraPreview(controller = controller, modifier = Modifier.fillMaxSize())

                IconButton(onClick = {
                    openCameraBottomSheet = false
                }, modifier = Modifier.offset(16.dp, 16.dp)) {
                    Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "navigate back")
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(16.dp), horizontalArrangement = Arrangement.SpaceAround
                ) {
                    IconButton(onClick = {
                        controller.cameraSelector =
                            if (controller.cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
                                CameraSelector.DEFAULT_FRONT_CAMERA
                            } else {
                                CameraSelector.DEFAULT_BACK_CAMERA
                            }
                    }) {
                        Icon(imageVector = Icons.Filled.Cameraswitch, contentDescription = "camera Switch")
                    }
                    IconButton(onClick = {
                        takePhoto(controller, addEditViewModel::onPhotoTaken, context)
                    }) {
                        Icon(imageVector = Icons.Filled.PhotoCamera, contentDescription = "Click To Capture")
                    }
                }

            }
        }
    }

}
fun takePhoto(controller: LifecycleCameraController, onPhotoTaken: (Bitmap) -> Unit, context: Context) {
    controller.takePicture(ContextCompat.getMainExecutor(context), object : OnImageCapturedCallback() {
        override fun onCaptureSuccess(image: ImageProxy) {
            super.onCaptureSuccess(image)
            val matrix = Matrix().apply {
                postRotate(image.imageInfo.rotationDegrees.toFloat())
                if (controller.cameraSelector == CameraSelector.DEFAULT_FRONT_CAMERA) {
                    postScale(-1f, 1f)
                }
            }
            val bitmap = Bitmap.createBitmap(image.toBitmap(), 0, 0, image.width, image.height, matrix, true)
            bitmap.toUri(context = context)
            onPhotoTaken(bitmap)
            Toast.makeText(context, "Attach Captured Photo from 'Add Image' option.", Toast.LENGTH_SHORT).show()
        }

        override fun onError(exception: ImageCaptureException) {
            super.onError(exception)
            Toast.makeText(context, "Something Went Wrong ! Try Again", Toast.LENGTH_SHORT).show()
        }
    })
}

fun Bitmap.toUri(context: Context, format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG) {
    val bytes = ByteArrayOutputStream()
    compress(format, 100, bytes)
    MediaStore.Images.Media.insertImage(
        context.contentResolver,
        this,
        "${System.currentTimeMillis()}",
        null
    )
}


