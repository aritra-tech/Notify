package com.aritra.notify.components.note

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aritra.notify.R
import com.aritra.notify.data.models.Note
import com.aritra.notify.utils.Const
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun NotesCard(
    noteModel: Note,
    navigateToUpdateNoteScreen: (noteId: Int) -> Unit
) {

    Card(
        modifier = Modifier
            .padding(2.dp)
            .fillMaxHeight()
            .clickable { navigateToUpdateNoteScreen(noteModel.id) },
        elevation = CardDefaults.cardElevation(3.dp),
        shape = RoundedCornerShape(8.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text(
                text = noteModel.title,
                fontSize = 20.sp,
                fontFamily = FontFamily(Font(R.font.poppins_semibold))
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = noteModel.note,
                fontSize = 18.sp,
                fontFamily = FontFamily(Font(R.font.poppins_light))
            )
            Spacer(modifier = Modifier.height(10.dp))
            val formattedDateTime =
                SimpleDateFormat(
                    Const.DATE_TIME_FORMAT,
                    Locale.getDefault()
                ).format(noteModel.dateTime)
            Text(
                text = formattedDateTime,
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.poppins_medium)),
                color = Color.Gray
            )
        }
    }
}