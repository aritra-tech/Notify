package com.aritra.notify.components.note

import TrashNoteInfo
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedAssistChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.aritra.notify.R
import com.aritra.notify.domain.models.Note
import com.aritra.notify.utils.Const
import com.aritra.notify.utils.formatReminderDateTime
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.Locale

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NotesCard(
    noteModel: Note,
    isSelected: Boolean,
    dateTimeDeleted: TrashNoteInfo? = null,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
) {
    val painter = rememberSaveable { mutableStateOf(noteModel.image) }
    val context = LocalContext.current

    OutlinedCard(
        border = CardDefaults.outlinedCardBorder().copy(0.dp),
        modifier = Modifier
            .padding(10.dp)
            .fillMaxHeight()
            .clip(RoundedCornerShape(15.dp))
            .combinedClickable(onClick = onClick, onLongClick = onLongClick),
        colors = CardDefaults.cardColors(
            if (isSelected) {
                MaterialTheme.colorScheme.surfaceVariant
            } else {
                MaterialTheme.colorScheme.surface
            }
        ),
        shape = RoundedCornerShape(15.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.TopEnd)
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                if (painter.value.isNotEmpty()) {
                    Row(
                        modifier = Modifier
                            .height(80.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        painter.value.forEach {
                            AsyncImage(
                                model = ImageRequest.Builder(context)
                                    .data(it ?: "")
                                    .build(),
                                contentDescription = "Image",
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }
                Text(
                    text = noteModel.title,
                    fontSize = 22.sp,
                    fontFamily = FontFamily(Font(R.font.poppins_medium)),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = noteModel.note,
                    fontSize = 18.sp,
                    fontFamily = FontFamily(Font(R.font.poppins_light)),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(10.dp))
                val formattedDateTime =
                    noteModel.dateTime?.let {
                        SimpleDateFormat(
                            Const.DATE_TIME_FORMAT,
                            Locale.getDefault()
                        ).format(it)
                    }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Column {
                        formattedDateTime?.let {
                            Text(
                                text = formattedDateTime,
                                fontSize = 16.sp,
                                fontFamily = FontFamily(Font(R.font.poppins_light)),
                                color = Color.Gray
                            )
                        }
                        noteModel.reminderDateTime?.let {
                            ReminderSection(it, noteModel.isReminded)
                        }
                    }
                    dateTimeDeleted?.let {
                        Text(
                            text = stringResource(id = R.string.format_deleted_note, it.formatDate),
                            fontSize = 16.sp,
                            fontFamily = FontFamily(Font(R.font.poppins_light)),
                            color = it.getDateColor()
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ReminderSection(
    dateTime: LocalDateTime,
    isReminded: Boolean = false,
) {
    ElevatedAssistChip(elevation = AssistChipDefaults.elevatedAssistChipElevation(4.dp), leadingIcon = {
        Icon(imageVector = Icons.Default.AccessTime, contentDescription = "")
    }, onClick = { /*TODO*/ }, label = {
        Text(
            text = dateTime.formatReminderDateTime(),
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            textDecoration = if (isReminded) TextDecoration.LineThrough else null,
            maxLines = 1
        )
    }, modifier = Modifier)
}
