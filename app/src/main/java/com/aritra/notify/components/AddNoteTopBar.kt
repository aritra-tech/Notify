package com.aritra.notify.components

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import com.aritra.notify.R
import com.aritra.notify.data.models.Note
import com.aritra.notify.screens.notes.addNoteScreen.AddNoteViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNoteTopBar(
    viewModel: AddNoteViewModel,
    navigateBack: () -> Unit,
    title: String,
    description: String
) {
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
