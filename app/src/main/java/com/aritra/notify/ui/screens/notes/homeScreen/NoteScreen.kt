@file:OptIn(ExperimentalMaterial3Api::class)

package com.aritra.notify.ui.screens.notes.homeScreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aritra.notify.R
import com.aritra.notify.components.actions.BackPressHandler
import com.aritra.notify.components.actions.LayoutToggleButton
import com.aritra.notify.components.note.GridNoteCard
import com.aritra.notify.components.note.NotesCard
import com.aritra.notify.components.appbar.SelectionModeTopAppBar
import com.aritra.notify.domain.models.Note
import com.aritra.notify.ui.screens.notes.addEditScreen.AddEditViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteScreen(
    onFabClicked: () -> Unit,
    navigateToUpdateNoteScreen: (noteId: Int) -> Unit,
    shouldHideBottomBar: (Boolean) -> Unit,
) {
    val viewModel = hiltViewModel<NoteScreenViewModel>()

    val addEditViewModel = hiltViewModel<AddEditViewModel>()
    val listOfAllNotes by viewModel.listOfNotes.observeAsState(listOf())
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var isGridView by rememberSaveable { mutableStateOf(false) }

    var isInSelectionMode by remember {
        mutableStateOf(false)
    }
    val selectedNoteIds = remember {
        mutableStateListOf<Int>()
    }

    val deletedNotes = remember {
        mutableStateListOf<Note>()
    }

    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }

    val resetSelectionMode = {
        isInSelectionMode = false
        selectedNoteIds.clear()
    }

    val listState: LazyListState = rememberLazyListState()
    val shouldHideSearchBarForList by remember(listState) {
        derivedStateOf {
            listState.firstVisibleItemIndex == 0
        }
    }

    val staggeredGState: LazyStaggeredGridState = rememberLazyStaggeredGridState()
    val shouldHideSearchBarForGrid by remember(staggeredGState) {
        derivedStateOf {
            staggeredGState.firstVisibleItemIndex == 0
        }
    }

    var shouldHideSearchBar by remember {
        mutableStateOf(true)
    }

    BackPressHandler(isInSelectionMode, resetSelectionMode)

    LaunchedEffect(
        key1 = isInSelectionMode,
        key2 = selectedNoteIds.size
    ) {
        if (isInSelectionMode && selectedNoteIds.isEmpty()) {
            isInSelectionMode = false
        }
    }

    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo }
            .collect {
                if (it.visibleItemsInfo.isNotEmpty()) {
                    shouldHideBottomBar.invoke(shouldHideSearchBarForList)
                    shouldHideSearchBar = shouldHideSearchBarForList
                }
            }
    }
    LaunchedEffect(staggeredGState) {
        snapshotFlow { staggeredGState.layoutInfo }
            .collect {
                if (it.visibleItemsInfo.isNotEmpty()) {
                    shouldHideBottomBar.invoke(shouldHideSearchBarForGrid)
                    shouldHideSearchBar = shouldHideSearchBarForGrid
                }
            }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onFabClicked() }
            ) {
                Icon(
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = "Add Notes"
                )
            }
        },

        topBar = {
            if (isInSelectionMode) {
                SelectionModeTopAppBar(
                    selectedItems = selectedNoteIds,
                    onDeleteClick = {
                        val selectedNotes =
                            listOfAllNotes.filter { note -> note.id in selectedNoteIds }

                        viewModel.deleteListOfNote(selectedNotes)

                        deletedNotes.addAll(selectedNotes)
                        resetSelectionMode()

                        scope.launch {
                            val snackBarResult = snackBarHostState.showSnackbar(
                                message = "Notes moved to trash",
                                actionLabel = "Undo",
                                duration = SnackbarDuration.Short,
                                withDismissAction = false
                            )

                            when (snackBarResult) {
                                SnackbarResult.ActionPerformed -> {
                                    addEditViewModel.insertListOfNote(deletedNotes) {}
                                }

                                SnackbarResult.Dismissed -> {
                                }
                            }
                        }
                    },

                    resetSelectionMode = resetSelectionMode
                )
            } else {
                AnimatedVisibility(
                    visible = shouldHideSearchBar,
                    enter = fadeIn(animationSpec = tween(delayMillis = 500, easing = LinearOutSlowInEasing)),
                    exit = fadeOut(animationSpec = tween(delayMillis = 500, easing = LinearOutSlowInEasing))
                ) {
                    SearchBar(
                        modifier = Modifier
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
                                    contentDescription = "Close"
                                )
                            } else {
                                LayoutToggleButton(
                                    isGridView = isGridView,
                                    onToggleClick = { isGridView = !isGridView }
                                )
                            }
                        }
                    ) {}
                }
            }
        },
        content = {
            Surface(
                modifier = Modifier.padding(it)
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    if (listOfAllNotes.isNotEmpty()) {
                        if (isGridView) {
                            LazyVerticalStaggeredGrid(
                                columns = StaggeredGridCells.Fixed(2),
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(start = 0.dp, top = 5.dp, end = 0.dp, bottom = 0.dp),
                                contentPadding = PaddingValues(bottom = 95.dp),
                                state = staggeredGState
                            ) {
                                itemsIndexed(
                                    listOfAllNotes.filter { note ->
                                        note.title.contains(searchQuery, true)
                                    }
                                ) { _, notesModel ->
                                    val isSelected = selectedNoteIds.contains(notesModel.id)

                                    GridNoteCard(
                                        notesModel,
                                        isSelected,
                                        {
                                            if (isInSelectionMode) {
                                                if (isSelected) {
                                                    selectedNoteIds.remove(notesModel.id)
                                                } else {
                                                    selectedNoteIds.add(notesModel.id)
                                                }
                                            } else {
                                                navigateToUpdateNoteScreen(notesModel.id)
                                            }
                                        }
                                    ) {
                                        if (isInSelectionMode) {
                                            if (isSelected) {
                                                selectedNoteIds.remove(notesModel.id)
                                            } else {
                                                selectedNoteIds.add(notesModel.id)
                                            }
                                        } else {
                                            isInSelectionMode = true
                                            selectedNoteIds.add(notesModel.id)
                                        }
                                    }
                                }
                            }
                        } else {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(start = 0.dp, top = 5.dp, end = 0.dp, bottom = 0.dp),
                                contentPadding = PaddingValues(bottom = 95.dp),
                                state = listState
                            ) {
                                items(
                                    listOfAllNotes.filter { note ->
                                        note.title.contains(searchQuery, true)
                                    }
                                ) { notesModel ->

                                    val isSelected = selectedNoteIds.contains(notesModel.id)

                                    Box {
                                        NotesCard(
                                            noteModel = notesModel,
                                            isSelected = isSelected,
                                            onClick = {
                                                if (isInSelectionMode) {
                                                    if (isSelected) {
                                                        selectedNoteIds.remove(notesModel.id)
                                                    } else {
                                                        selectedNoteIds.add(notesModel.id)
                                                    }
                                                } else {
                                                    navigateToUpdateNoteScreen(notesModel.id)
                                                }
                                            }
                                        ) {
                                            if (isInSelectionMode) {
                                                if (isSelected) {
                                                    selectedNoteIds.remove(notesModel.id)
                                                } else {
                                                    selectedNoteIds.add(notesModel.id)
                                                }
                                            } else {
                                                isInSelectionMode = true
                                                selectedNoteIds.add(notesModel.id)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        NoList(
                            image = painterResource(id = R.drawable.no_list),
                            contentDescription = stringResource(R.string.no_notes_added),
                            message = stringResource(R.string.click_on_the_compose_button_to_add)
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun NoList(image: Painter, contentDescription: String, message: String) {
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier.fillMaxWidth(),
            painter = image,
            contentDescription = "null"
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = contentDescription,
            textAlign = TextAlign.Center,
            fontFamily = FontFamily(
                Font(R.font.poppins_medium)
            )
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = message,
            textAlign = TextAlign.Center,
            fontFamily = FontFamily(Font(R.font.poppins_light))
        )
    }
}
