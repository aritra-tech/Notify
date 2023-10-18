package com.aritra.notify.components.topbar

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.aritra.notify.domain.models.Note
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aritra.notify.R
import com.aritra.notify.components.actions.ShareOption
import com.aritra.notify.components.dialog.TextDialog
import com.aritra.notify.ui.screens.notes.homeScreen.NoteScreenViewModel
import com.aritra.notify.utils.shareAsImage
import com.aritra.notify.utils.shareAsPdf
import com.aritra.notify.utils.shareNoteAsText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTopBar(
    note: Note?,
    title: String,
    description: String,
    onBackPress: () -> Unit,
    saveNote: () -> Unit,
    updateNote: () -> Unit,
) {
    val noteScreenViewModel = hiltViewModel<NoteScreenViewModel>()
    var showSheet by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val skipPartiallyExpanded by remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = skipPartiallyExpanded
    )
    val view = LocalView.current
    val bitmapSize = view.width to view.height
    val deleteDialogVisible = remember { mutableStateOf(false) }

    BackHandler(onBack = {
        if (note != null) {
            updateNote()
        } else {
            onBackPress()
        }
    })

    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        title = {
            Text(
                text = "",
                fontFamily = FontFamily(Font(R.font.poppins_semibold))
            )
        },
        navigationIcon = {
            IconButton(onClick = {
                if (note != null) {
                    updateNote()
                } else {
                    onBackPress()
                }
            }) {
                Icon(
                    painterResource(R.drawable.back),
                    contentDescription = stringResource(R.string.back)
                )
            }
        },
        actions = {
            note?.let {
                IconButton(onClick = { deleteDialogVisible.value = true }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_delete),
                        contentDescription = "Delete"
                    )
                }

                if (deleteDialogVisible.value) {
                    TextDialog(
                        title = stringResource(R.string.warning),
                        description = stringResource(
                            R.string.are_you_sure_want_to_delete_these_items_it_cannot_be_recovered
                        ),
                        isOpened = deleteDialogVisible.value,
                        onDismissCallback = { deleteDialogVisible.value = false },
                        onConfirmCallback = {
                            noteScreenViewModel.deleteNote(note)
                            deleteDialogVisible.value = false
                            onBackPress()
                        }
                    )
                }
            }
            if (title.isNotEmpty() && description.isNotEmpty()) {
                IconButton(onClick = { showSheet = true }) {
                    Icon(
                        painterResource(R.drawable.ic_share),
                        contentDescription = stringResource(R.string.share)
                    )
                }
                if (showSheet) {
                    ModalBottomSheet(
                        onDismissRequest = { showSheet = false },
                        sheetState = bottomSheetState
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .navigationBarsPadding()
                                .padding(16.dp)
                        ) {
                            Spacer(modifier = Modifier.height(5.dp))
                            ShareOption(
                                text = stringResource(R.string.share_note_as_text),
                                icon = painterResource(id = R.drawable.text_icon),
                                onClick = {
                                    shareNoteAsText(context, title, description)
                                    showSheet = false
                                }
                            )
                            Spacer(modifier = Modifier.height(15.dp))
                            ShareOption(
                                text = stringResource(R.string.share_note_as_picture),
                                icon = painterResource(id = R.drawable.image_icon),
                                onClick = {
                                    shareAsImage(view, bitmapSize)
                                    showSheet = false
                                }
                            )
                            Spacer(modifier = Modifier.height(15.dp))
                            ShareOption(
                                text = stringResource(R.string.share_as_pdf),
                                icon = painterResource(id = R.drawable.pdf_icon),
                                onClick = {
                                    shareAsPdf(view, "Notify")
                                    showSheet = false
                                }
                            )
                        }
                    }
                }
                IconButton(onClick = {
                    if (note != null) {
                        updateNote()
                    } else {
                        saveNote()
                    }
                }) {
                    Icon(
                        painterResource(R.drawable.save),
                        contentDescription = stringResource(R.string.save)
                    )
                }
            }
        }
    )
}
