@file:OptIn(ExperimentalMaterial3Api::class)

package com.aritra.notify.ui.screens.notes.homeScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DismissDirection.*
import androidx.compose.material3.DismissValue.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aritra.notify.R
import com.aritra.notify.components.actions.NoList
import com.aritra.notify.components.dialog.TextDialog
import com.aritra.notify.components.topbar.TopBar
import com.aritra.notify.data.models.Note
import com.aritra.notify.utils.Const
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

    val scrollState = rememberLazyListState()
    val fabVisibleState = remember {
        mutableStateOf(scrollState.firstVisibleItemIndex > 0)
    }

    Scaffold(
        topBar = { TopBar() },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text(text = stringResource(R.string.compose)) },
                icon = {
                    Icon(
                        imageVector = Icons.Outlined.Edit,
                        contentDescription = null
                    )
                },
                onClick = { onFabClicked() },
                expanded = fabVisibleState.value.not()
            )
        },
    ) {
        Surface(
            modifier = Modifier.padding(it)
        ) {

            Column(modifier = Modifier.fillMaxSize()) {

                SearchBar(
                    modifier = Modifier
                        .align(Alignment.Start)
                        .fillMaxWidth()
                        .padding(10.dp),
                    query = searchQuery,
                    onQueryChange = { search ->
                        searchQuery = search
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

                if (listOfAllNotes.isNotEmpty()) {

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(0.dp, 5.dp, 0.dp, 0.dp),
                        state = scrollState
                    ) {

                        items(listOfAllNotes.filter { note ->
                            note.title.contains(searchQuery, true)
                        }) { notesModel ->
                            SwapDelete(notesModel, viewModel, navigateToUpdateNoteScreen)
                        }
                    }
                } else {
                    NoList(
                        contentDescription = stringResource(R.string.no_notes_added),
                        message = stringResource(R.string.click_on_the_compose_button_to_add)
                    )
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
                painter = painterResource(R.drawable.ic_delete),
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

