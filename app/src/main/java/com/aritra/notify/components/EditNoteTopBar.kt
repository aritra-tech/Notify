package com.aritra.notify.components

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import com.aritra.notify.R
import com.aritra.notify.data.models.Note
import com.aritra.notify.screens.addNoteScreen.AddNoteViewModel
import com.aritra.notify.screens.editNoteScreen.EditScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditNoteTopBar(
    viewModel: EditScreenViewModel,
    noteId: Int,
    navigateBack: () -> Unit,
    title: String,
    description: String
){
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.onSecondary
        ),
        title = {
            Text(
                text = "",
                fontFamily = FontFamily(Font(R.font.poppins_semibold))
            )
        },
        navigationIcon = {
            IconButton(onClick = { navigateBack() }) {
                Icon(
                    painterResource(R.drawable.back),
                    contentDescription = "back"
                )
            }
        },
        actions = {

            IconButton(onClick = { TODO() }) {
                Icon(
                    painterResource(R.drawable.share),
                    contentDescription = "share"
                )
            }
            IconButton(onClick = {
                val updateNote = Note(noteId,title,description)
                viewModel.updateNotes(updateNote)
                navigateBack()
            }) {
                Icon(
                    painterResource(R.drawable.save),
                    contentDescription = "save"
                )
            }

        }
    )
}