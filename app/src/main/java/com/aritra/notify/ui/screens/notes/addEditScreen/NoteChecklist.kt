package com.aritra.notify.ui.screens.notes.addEditScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.aritra.notify.R
import com.aritra.notify.domain.models.Todo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteChecklist(
    checklist: List<Todo>,
    modifier: Modifier = Modifier,
    onAdd: (String) -> Unit,
    onDelete: (Int) -> Unit,
    onUpdate: (Int, String) -> Unit,
    onToggle: (Int) -> Unit,
    onDismiss: () -> Unit,
) {
    var showAddModal by remember {
        mutableStateOf(false)
    }

    ModalBottomSheet(
        modifier = modifier,
        onDismissRequest = onDismiss,
        dragHandle = null,
        content = {
            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(
                        title = {
                            Text("Checklist")
                        },
                        actions = {
                            IconButton(
                                onClick = {
                                    showAddModal = true
                                },
                                modifier = Modifier.padding(8.dp),
                                content = {
                                    Icon(
                                        painter = painterResource(id = R.drawable.add_box_icon),
                                        contentDescription = "Add"
                                    )
                                }
                            )
                        }
                    )
                },
                content = { scaffoldPadding ->
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(scaffoldPadding),
                        contentPadding = PaddingValues(16.dp),
                        content = {
                            items(
                                count = checklist.size,
                                key = { index -> checklist[index] },
                                itemContent = { index ->
                                    ChecklistItem(
                                        todo = checklist[index],
                                        onValueChange = {
                                            onUpdate(index, it)
                                        },
                                        onCheckedChange = {
                                            onToggle(index)
                                        },
                                        onDelete = {
                                            onDelete(index)
                                        }
                                    )
                                    if (index < checklist.lastIndex) {
                                        Divider()
                                    }
                                }
                            )
                        }
                    )
                }
            )
        }
    )

    if (showAddModal) {
        AddEditTodoField(
            text = "",
            onUpdate = onAdd,
            onDismiss = {
                showAddModal = false
            }
        )
    }
}

@Composable
private fun ChecklistItem(
    todo: Todo,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit,
    onCheckedChange: (Boolean) -> Unit,
    onDelete: () -> Unit,
) {
    var showEditModal by remember {
        mutableStateOf(false)
    }
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        content = {
            Checkbox(
                checked = todo.isChecked,
                onCheckedChange = onCheckedChange
            )
            Text(
                todo.title,
                modifier = Modifier.weight(1f),
                style = TextStyle(
                    textDecoration = if (todo.isChecked) TextDecoration.LineThrough else TextDecoration.None
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            IconButton(
                onClick = {
                    showEditModal = true
                },
                content = {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit"
                    )
                }
            )
            IconButton(
                onClick = onDelete,
                content = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_delete),
                        contentDescription = "Delete"
                    )
                }
            )
        }
    )

    if (showEditModal) {
        AddEditTodoField(
            text = todo.title,
            onUpdate = onValueChange,
            onDismiss = {
                showEditModal = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddEditTodoField(
    text: String,
    onUpdate: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    val focusRequester = remember { FocusRequester() }
    var value by remember(text) {
        mutableStateOf(text)
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color.Transparent,
        dragHandle = null,
        shape = RectangleShape,
        content = {
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                value = value,
                onValueChange = {
                    value = it
                },
                singleLine = true,
                keyboardActions = KeyboardActions(
                    onDone = {
                        if (value.isNotBlank()) {
                            onUpdate(value)
                        }
                        onDismiss()
                    }
                ),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Done
                )
            )
        }
    )
}
