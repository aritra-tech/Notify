package com.aritra.notify.components.appbar

import android.widget.Toast
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
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.unit.dp
import com.aritra.notify.R
import com.aritra.notify.components.actions.ShareOption
import com.aritra.notify.components.dialog.TextDialog
import com.aritra.notify.utils.shareAsImage
import com.aritra.notify.utils.shareAsPdf
import com.aritra.notify.utils.shareNoteAsText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTopBar(
    title: String,
    description: String,
    isNew: Boolean,
    onBackPress: () -> Unit,
    saveNote: () -> Unit,
    deleteNote: (() -> Unit) -> Unit,
) {
    val context = LocalContext.current
    val deleteDialogVisible = remember { mutableStateOf(false) }

    val onBack = remember(description) {
        {
            if (isNew) {
                onBackPress()
            } else if (description.isBlank()) {
                Toast.makeText(
                    context,
                    "Your note cannot be blank",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                saveNote()
            }
        }
    }

    BackHandler(onBack = onBack)

    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        title = {},
        navigationIcon = {
            IconButton(
                onClick = onBack,
                content = {
                    Icon(
                        painterResource(R.drawable.back),
                        contentDescription = stringResource(R.string.back)
                    )
                }
            )
        },
        actions = {
            if (!isNew) {
                IconButton(
                    onClick = {
                        deleteDialogVisible.value = true
                    },
                    content = {
                        Icon(
                            painter = painterResource(R.drawable.ic_delete),
                            contentDescription = "Delete"
                        )
                    }
                )

                if (deleteDialogVisible.value) {
                    TextDialog(
                        title = stringResource(R.string.warning),
                        description = stringResource(
                            R.string.are_you_sure_want_to_delete_these_items_it_cannot_be_recovered
                        ),
                        isOpened = deleteDialogVisible.value,
                        onDismissCallback = {
                            deleteDialogVisible.value = false
                        },
                        onConfirmCallback = {
                            deleteNote {
                                deleteDialogVisible.value = false
                                onBackPress()
                            }
                        }
                    )
                }
            }
            if (description.isNotBlank()) {
                ShareNote(title, description)
                IconButton(
                    onClick = saveNote,
                    content = {
                        Icon(
                            painterResource(R.drawable.save),
                            contentDescription = stringResource(R.string.save)
                        )
                    }
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ShareNote(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
) {
    val view = LocalView.current
    val context = LocalContext.current

    var showSheet by remember { mutableStateOf(false) }

    IconButton(
        modifier = modifier,
        onClick = {
            showSheet = true
        },
        content = {
            Icon(
                painterResource(R.drawable.ic_share),
                contentDescription = stringResource(R.string.share)
            )
        }
    )
    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showSheet = false
            },
            content = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(16.dp),
                    content = {
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
                                shareAsImage(view, view.width to view.height)
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
                )
            }
        )
    }
}
