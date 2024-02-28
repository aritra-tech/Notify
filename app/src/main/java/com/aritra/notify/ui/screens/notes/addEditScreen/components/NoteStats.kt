package com.aritra.notify.ui.screens.notes.addEditScreen.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.aritra.notify.R
import com.aritra.notify.ui.theme.NotifyTheme
import com.aritra.notify.utils.Const
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.ceil

@Composable
fun NoteStats(
    title: String,
    description: String,
    dateTime: Date?,
    modifier: Modifier = Modifier,
) {
    val textStyle = TextStyle(
        fontSize = 15.sp,
        fontFamily = FontFamily(Font(R.font.poppins_light))
    )
    val colors = TextFieldDefaults.colors(
        focusedContainerColor = MaterialTheme.colorScheme.surface,
        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
        disabledContainerColor = MaterialTheme.colorScheme.surface,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.Transparent
    )

    val formattedDateTime = remember(dateTime) {
        SimpleDateFormat(
            Const.DATE_TIME_FORMAT,
            Locale.getDefault()
        ).format(dateTime ?: Date())
    }
    val formattedCharacterCount = remember(title, description) {
        "${(title.trim().length) + (description.trim().length)} characters"
    }
    val formattedWordCount = remember(description) {
        "${countWords(description)} words"
    }
    val formattedReadTime = remember(description) {
        "${calculateReadTime(countWords(description))} sec read"
    }

    Column(
        modifier = modifier,
        content = {
            TextField(
                value = "$formattedDateTime   |  $formattedReadTime",
                onValueChange = { },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                textStyle = textStyle,
                colors = colors,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text
                )
            )
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = "$formattedCharacterCount | $formattedWordCount",
                onValueChange = { },
                readOnly = true,
                textStyle = textStyle,
                colors = colors,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text
                )
            )
        }
    )
}

private fun countWords(text: String): Int {
    val words = text.split(Regex("\\s+"))
    return words.count { it.isNotBlank() }
}

private fun calculateReadTime(words: Int, wordsPerMinute: Int = 238): Int {
    val minutes = words / wordsPerMinute.toDouble()
    return ceil(minutes * 60).toInt() // Convert to seconds
}

@Preview(showBackground = true)
@Composable
fun NoteStatsPreview() = NotifyTheme {
    NoteStats(
        title = "Title",
        description = "Description",
        dateTime = Date()
    )
}
