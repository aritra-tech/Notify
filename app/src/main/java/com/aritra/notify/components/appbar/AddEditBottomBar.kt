package com.aritra.notify.components.appbar

import android.Manifest
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.aritra.notify.R
import com.aritra.notify.components.actions.BottomSheetOptions
import com.aritra.notify.components.actions.SpeechRecognizerContract
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class, ExperimentalLayoutApi::class)
@Composable
fun AddEditBottomBar(
    modifier: Modifier = Modifier,
    onImagesSelected: (List<Uri>) -> Unit,
    onSpeechRecognized: (String) -> Unit,
    showDrawingScreen: () -> Unit,
    showCameraSheet: () -> Unit,
    onReminderDateTime: () -> Unit,
    addTodo: () -> Unit,
) {
    var showSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    val bottomPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    val imeIsVisible = WindowInsets.isImeVisible
    val microphonePermissionState = rememberPermissionState(Manifest.permission.RECORD_AUDIO)
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)

    val imageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(),
        onResult = { uris ->
            onImagesSelected(uris)
        }
    )
    val speechLauncher = rememberLauncherForActivityResult(
        contract = SpeechRecognizerContract(),
        onResult = { words ->
            if (words.isNullOrEmpty()) {
                return@rememberLauncherForActivityResult
            }
            onSpeechRecognized(words.joinToString(separator = " "))
        }
    )

    BottomAppBar(
        modifier = modifier
            .navigationBarsPadding()
            .imePadding(),
        containerColor = Color.Transparent,
        content = {
            IconButton(
                onClick = { showSheet = true },
                content = {
                    Icon(
                        modifier = Modifier.size(25.dp),
                        painter = painterResource(id = R.drawable.add_box_icon),
                        contentDescription = stringResource(R.string.add_box)
                    )
                }
            )
            IconButton(onClick = { onReminderDateTime() }) {
                Icon(
                    modifier = Modifier.size(25.dp),
                    painter = painterResource(id = R.drawable.add_alert),
                    contentDescription = stringResource(R.string.add_box)
                )
            }
            if (showSheet) {
                ModalBottomSheet(
                    onDismissRequest = { showSheet = false },
                    sheetState = sheetState,
                    dragHandle = { BottomSheetDefaults.DragHandle() },
                    windowInsets = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) WindowInsets.ime
                    else WindowInsets(0,0,0,0),
                    content = {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = if (imeIsVisible) 0.dp else bottomPadding)
                                .padding(16.dp),
                            content = {
                                BottomSheetOptions(
                                    text = stringResource(R.string.take_image),
                                    icon = painterResource(id = R.drawable.camera),
                                    onClick = {
                                        if (cameraPermissionState.status.isGranted) {
                                            showCameraSheet()
                                        } else {
                                            cameraPermissionState.launchPermissionRequest()
                                        }
                                        showSheet = false
                                    }
                                )
                                BottomSheetOptions(
                                    text = stringResource(R.string.add_image),
                                    icon = painterResource(id = R.drawable.gallery),
                                    onClick = {
                                        imageLauncher.launch(
                                            PickVisualMediaRequest(
                                                mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
                                            )
                                        )
                                        showSheet = false
                                    }
                                )
                                BottomSheetOptions(
                                    text = stringResource(R.string.drawing),
                                    icon = painterResource(id = R.drawable.drawing),
                                    onClick = {
                                        showDrawingScreen()
                                        showSheet = false
                                    }
                                )
                                BottomSheetOptions(
                                    text = stringResource(R.string.speech_to_text),
                                    icon = painterResource(id = R.drawable.mic_icon),
                                    onClick = {
                                        if (microphonePermissionState.status.isGranted) {
                                            speechLauncher.launch(Unit)
                                        } else {
                                            microphonePermissionState.launchPermissionRequest()
                                        }
                                        showSheet = false
                                    }
                                )
                                BottomSheetOptions(
                                    text = stringResource(R.string.add_todo),
                                    icon = painterResource(id = R.drawable.add_box_icon),
                                    onClick = {
                                        addTodo()
                                        showSheet = false
                                    }
                                )
                            }
                        )
                    }
                )
            }
        }
    )
}
