package com.aritra.notify.components.note

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.aritra.notify.R
import com.aritra.notify.domain.models.Note
import com.aritra.notify.utils.Const
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun NotesCard(
    noteModel: Note,
    navigateToUpdateNoteScreen: (noteId: Int) -> Unit
) {
    val painter = rememberSaveable { mutableStateOf(noteModel.image) }
    val context = LocalContext.current

    OutlinedCard(
        border = CardDefaults.outlinedCardBorder().copy(0.dp),
        modifier = Modifier
            .padding(10.dp)
            .fillMaxHeight()
            .clickable { navigateToUpdateNoteScreen(noteModel.id) },
        shape = RoundedCornerShape(15.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            if (painter.value.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .height(80.dp)
                        .clip(RoundedCornerShape(8.dp))
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
            Spacer(modifier = Modifier.height(10.dp))
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
            formattedDateTime?.let {
                Text(
                    text = formattedDateTime,
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.poppins_light)),
                    color = Color.Gray
                )
            }
        }
    }
}