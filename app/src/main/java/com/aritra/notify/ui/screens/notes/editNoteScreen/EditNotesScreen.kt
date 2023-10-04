package com.aritra.notify.ui.screens.notes.editNoteScreen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aritra.notify.R
import com.aritra.notify.components.topbar.AddEditTopBar
import com.aritra.notify.domain.models.Note
import com.aritra.notify.utils.Const
import me.saket.telephoto.zoomable.coil.ZoomableAsyncImage
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun EditNotesScreen(
    noteId: Int,
    navigateBack: () -> Unit
) {
    val note = Note(noteId, "", "", Date(), emptyList())
    val context = LocalContext.current
    val editViewModel = hiltViewModel<EditScreenViewModel>()
    val title = editViewModel.noteModel.observeAsState().value?.title ?: ""
    val description = editViewModel.noteModel.observeAsState().value?.note ?: ""
    val imagePathList = editViewModel.noteModel.observeAsState().value?.image ?: emptyList()
    val dateTime = editViewModel.noteModel.observeAsState().value?.dateTime
    val focus = LocalFocusManager.current
    val formattedDateTime =
        SimpleDateFormat(Const.DATE_TIME_FORMAT, Locale.getDefault()).format(dateTime ?: 0)
    val formattedCharacterCount = "${(title.length) + (description.length)} characters"

    val saveNote: () -> Unit = remember {
        {
            editViewModel.updateNotes {
                navigateBack()
                Toast.makeText(context, "Successfully Updated!", Toast.LENGTH_SHORT).show()
            }
            navigateBack()
        }
    }

    LaunchedEffect(Unit) {
        editViewModel.getNoteById(noteId)
    }
    Scaffold(
        topBar = {
            if (dateTime != null) {
                AddEditTopBar(
                    note,
                    onBackPress = navigateBack,
                    title = title,
                    description = description,
                    updateNote = saveNote,
                    saveNote = {}
                )
            }
        }
    ) {
        Box(
            modifier = Modifier.padding(it)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                if (imagePathList.isNotEmpty()) {
                    LazyRow(
                        modifier = Modifier
                            .height(240.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(imagePathList) { image ->
                            ZoomableAsyncImage(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .width((LocalConfiguration.current.screenWidthDp * 0.8).dp),
                                model = image,
                                contentDescription = stringResource(R.string.image),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }

                TextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    value = title,
                    onValueChange = { newTitle ->
                        editViewModel.updateTitle(newTitle)
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
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        disabledContainerColor = MaterialTheme.colorScheme.surface,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
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
                    value = "$formattedDateTime  |  $formattedCharacterCount",
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
                        disabledIndicatorColor = Color.Transparent,
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Text,
                    )
                )
                TextField(
                    modifier = Modifier.fillMaxSize(),
                    value = description,
                    onValueChange = { newDescription ->
                        editViewModel.updateDescription(newDescription)
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
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        disabledContainerColor = MaterialTheme.colorScheme.surface,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        capitalization = KeyboardCapitalization.Sentences,
                        keyboardType = KeyboardType.Text,
                    ),
                )
            }
        }
    }
}