package com.aritra.notify.components

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import com.aritra.notify.R
import com.aritra.notify.data.models.Note
import com.aritra.notify.screens.addNoteScreen.AddNoteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNoteTopBar(
    viewModel: AddNoteViewModel,
    navigateBack: () -> Unit,
    title: String,
    description: String
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "Add notes",
                fontFamily = FontFamily(Font(R.font.poppins_semibold))
            )
        },
        navigationIcon = {
            IconButton(onClick = { TODO() }) {
                Icon(
                    painterResource(R.drawable.back),
                    contentDescription = "back"
                )
            }
        },
        actions = {

            IconButton(onClick = { navigateBack() }) {
                Icon(
                    painterResource(R.drawable.share),
                    contentDescription = "share"
                )
            }

            IconButton(onClick = {
                if (title.isNotEmpty() || description.isNotEmpty()) {
                    val noteDB = Note(id = 0, title = title, note = description)
                    viewModel.insertNote(noteDB)
                    navigateBack()
                }
                else {
                    navigateBack()
                }
            }) {
                Icon(
                    painterResource(R.drawable.save),
                    contentDescription = "save"
                )
            }

        }
    )
}
