package com.aritra.notify.ui.screens.notes.addNoteScreen


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aritra.notify.R
import com.aritra.notify.components.topbar.AddNoteTopBar
import com.aritra.notify.components.dialog.TextDialog
import com.aritra.notify.utils.Const
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun AddNotesScreen(
    navigateBack: () -> Unit
) {
    val addViewModel = hiltViewModel<AddNoteViewModel>()
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    val dateTime by remember { mutableStateOf(Calendar.getInstance().time) }
    var characterCount by remember { mutableIntStateOf(title.length + description.length) }
    val cancelDialogState = remember { mutableStateOf(false) }
    val dateFormat = SimpleDateFormat(Const.DATE_FORMAT, Locale.getDefault())
    val timeFormat = SimpleDateFormat(Const.TIME_FORMAT, Locale.getDefault())
    timeFormat.isLenient = false
    val currentDate = dateFormat.format(Calendar.getInstance().time)
    val currentTime = timeFormat.format(Calendar.getInstance().time).uppercase(Locale.getDefault())
    val focus = LocalFocusManager.current


    Scaffold(
        topBar = {
            AddNoteTopBar(
                addViewModel,
                onBackPress = { cancelDialogState.value = true },
                onSave = { navigateBack() },
                title,
                description,
                dateTime,
            )
        },
    ) {
        Surface(
            modifier = Modifier.padding(it)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                TextField(
                    modifier = Modifier
                        .fillMaxWidth(),
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
                        fontSize = 20.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_medium)),
                    ),
                    maxLines = Int.MAX_VALUE,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.onSecondary,
                        unfocusedContainerColor = MaterialTheme.colorScheme.onSecondary,
                        disabledContainerColor = MaterialTheme.colorScheme.onSecondary,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
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
                        focusedContainerColor = MaterialTheme.colorScheme.onSecondary,
                        unfocusedContainerColor = MaterialTheme.colorScheme.onSecondary,
                        disabledContainerColor = MaterialTheme.colorScheme.onSecondary,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
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
                        focusedContainerColor = MaterialTheme.colorScheme.onSecondary,
                        unfocusedContainerColor = MaterialTheme.colorScheme.onSecondary,
                        disabledContainerColor = MaterialTheme.colorScheme.onSecondary,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
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