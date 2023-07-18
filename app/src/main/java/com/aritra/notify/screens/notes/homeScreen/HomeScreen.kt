@file:OptIn(ExperimentalMaterial3Api::class)

package com.aritra.notify.screens.notes.homeScreen


import android.view.MotionEvent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DismissDirection.*
import androidx.compose.material3.DismissValue.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aritra.notify.R
import com.aritra.notify.components.dialog.TextDialog
import com.aritra.notify.components.topbar.TopBar
import com.aritra.notify.data.models.Note
import com.aritra.notify.utils.Const
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox
import java.text.SimpleDateFormat
import java.util.Locale


@Composable
fun HomeScreen(
    onFabClicked: () -> Unit,
    navigateToUpdateNoteScreen: (noteId: Int) -> Unit
) {

    val viewModel = hiltViewModel<HomeScreenViewModel>()
    val listOfAllNotes by viewModel.listOfNotes.observeAsState(listOf())
    var searchQuery by rememberSaveable { mutableStateOf("") }

    Scaffold(
        topBar = { TopBar() },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.padding(0.dp, 0.dp, 20.dp, 5.dp),
                onClick = { onFabClicked() }
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = "Add FAB",
                    tint = Color.White,
                )
            }
        },
        containerColor = colorScheme.surface
    ) {
        Surface(
            modifier = Modifier.padding(it)
        ) {

            Column(modifier = Modifier.fillMaxSize()) {

                SearchBar(
                    modifier = Modifier
                        .align(Alignment.Start)
                        .fillMaxWidth()
                        .padding(20.dp, 0.dp),
                    query = searchQuery,
                    onQueryChange = {
                        searchQuery = it
                    },
                    onSearch = {},
                    active = false,
                    onActiveChange = {},
                    placeholder = { Text(stringResource(R.string.search_your_notes)) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            Icon(
                                modifier = Modifier.clickable { searchQuery = "" },
                                imageVector = Icons.Default.Close,
                                contentDescription = null
                            )
                        }
                    }
                ) {
                }
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(0.dp, 5.dp, 0.dp, 0.dp)
                ) {

                    if (listOfAllNotes.isNotEmpty()) {
                        items(listOfAllNotes.filter { note ->
                            note.title.contains(searchQuery,true)
                        }) { notesModel ->
                            SwapDelete(notesModel, viewModel, navigateToUpdateNoteScreen)
                        }
                    } else {
                        item {
                            NoList()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SwapDelete(
    notesModel: Note,
    viewModel: HomeScreenViewModel,
    navigateToUpdateNoteScreen: (noteId: Int) -> Unit
) {

    val deleteDialogVisible = remember { mutableStateOf(false) }

    val delete = SwipeAction(
        onSwipe = {
            deleteDialogVisible.value = true
        },
        icon = {
            Icon(
                modifier = Modifier.padding(12.dp),
                imageVector = Icons.Default.Delete,
                contentDescription = null,
                tint = Color.White
            )
        },
        background = Color.Red,
    )

    SwipeableActionsBox(
        modifier = Modifier.padding(10.dp),
        swipeThreshold = 100.dp,
        endActions = listOf(delete)
    ) {
        NotesCard(notesModel, navigateToUpdateNoteScreen)

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
        colors = CardDefaults.cardColors(
            containerColor = colorScheme.onSecondary
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text(
                text = noteModel.title,
                fontSize = 22.sp,
                fontFamily = FontFamily(Font(R.font.poppins_semibold))
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = noteModel.note,
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.poppins_light))
            )
            Spacer(modifier = Modifier.height(10.dp))
            val formattedDateTime =
                SimpleDateFormat(Const.DATE_TIME_FORMAT, Locale.getDefault()).format(noteModel.dateTime)
            Text(
                text = formattedDateTime,
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.poppins_medium)),
                color = Color.Gray
            )
        }
    }
}

@Composable
fun NoList() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp, 130.dp, 0.dp, 0.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.no_list),
            contentDescription = "empty",
            modifier = Modifier.fillMaxWidth(),
            alignment = Alignment.Center
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "No notes found",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontFamily = FontFamily(Font(R.font.poppins_semibold)),
            fontSize = 20.sp
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "When you add a notes. You will see your notes here",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontFamily = FontFamily(Font(R.font.poppins_light)),
            fontSize = 18.sp
        )
    }
}

