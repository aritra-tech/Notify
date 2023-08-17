package com.aritra.notify.components.note

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.aritra.notify.R
import com.aritra.notify.components.dialog.TextDialog
import com.aritra.notify.data.models.Note
import com.aritra.notify.ui.screens.notes.homeScreen.NoteScreenViewModel
import com.aritra.notify.utils.Const
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun GridNoteCard(
    notesModel: Note,
    viewModel: NoteScreenViewModel,
    navigateToUpdateNoteScreen: (noteId: Int) -> Unit,
    isGridView: Boolean
) {
    var expanded by remember { mutableStateOf(false) }
    val deleteDialogVisible = remember { mutableStateOf(false) }
    val painter = rememberSaveable { mutableStateOf(notesModel.imagePath) }
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .clickable { navigateToUpdateNoteScreen(notesModel.id) },
        elevation = CardDefaults.cardElevation(3.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp).fillMaxWidth()
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(painter.value)
                    .build(),
                contentDescription = "Image",
                modifier = Modifier.fillMaxWidth()

            )
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.weight(2f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.padding(top = 10.dp),
                        text = notesModel.title,
                        fontSize = 20.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_semibold)),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                if (isGridView) {
                    IconButton(
                        onClick = { expanded = true }
                    ) {
                        Icon(Icons.Default.MoreVert, "Options")
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                    ) {
                        DropdownMenuItem(
                            text = { Text("Delete") },
                            onClick = {
                                deleteDialogVisible.value = true
                                expanded = false
                            },
                            leadingIcon = {
                                Icon(
                                    modifier = Modifier.padding(12.dp),
                                    imageVector = Icons.Outlined.Delete,
                                    contentDescription = null
                                )
                            }
                        )
                    }
                    if (deleteDialogVisible.value) {
                        TextDialog(
                            title = stringResource(R.string.warning),
                            description = stringResource(R.string.are_you_sure_want_to_delete_these_items_it_cannot_be_recovered),
                            isOpened = deleteDialogVisible.value,
                            onDismissCallback = { deleteDialogVisible.value = false },
                            onConfirmCallback = {
                                viewModel.deleteNote(notesModel)
                                deleteDialogVisible.value = false
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = notesModel.note,
                fontSize = 18.sp,
                fontFamily = FontFamily(Font(R.font.poppins_light)),
                maxLines = 4,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(10.dp))
            val formattedDateTime =
                SimpleDateFormat(
                    Const.DATE_TIME_FORMAT,
                    Locale.getDefault()
                ).format(notesModel.dateTime)
            Text(
                text = formattedDateTime,
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.poppins_medium)),
                color = Color.Gray
            )
        }
    }
}