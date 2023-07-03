package com.aritra.notify.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aritra.notify.R
import com.aritra.notify.data.models.Note
import com.aritra.notify.screens.notes.addNoteScreen.AddNoteViewModel
import eu.wewox.modalsheet.ExperimentalSheetApi
import eu.wewox.modalsheet.ModalSheet
import eu.wewox.modalsheet.ModalSheetDefaults
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class, ExperimentalSheetApi::class)
@Composable
fun AddNoteTopBar(
    viewModel: AddNoteViewModel,
    navigateBack: () -> Unit,
    title: String,
    description: String,
) {
    var showSheet by remember { mutableStateOf(false) }
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
            if (title.isNotEmpty() && description.isNotEmpty()) {
                IconButton(onClick = { showSheet = true }) {
                    Icon(
                        painterResource(R.drawable.share),
                        contentDescription = "share"
                    )
                }
                ModalSheet(
                    visible = showSheet,
                    onVisibleChange = { showSheet = it},
                    backgroundColor = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(15.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .navigationBarsPadding()
                            .padding(16.dp)
                    ) {
                        Spacer(modifier = Modifier.height(15.dp))
                        Text(
                            text = "Share note as text",
                            fontSize = 18.sp,
                            fontFamily = FontFamily(Font(R.font.poppins_medium)),
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Share note as picture",
                            fontSize = 18.sp,
                            fontFamily = FontFamily(Font(R.font.poppins_medium)),
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Button(onClick = { showSheet = false },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.outline
                            )
                        ) {
                            Text(
                                text = "Cancel",
                                fontSize = 16.sp,
                                textAlign = TextAlign.Center,
                                fontFamily = FontFamily(Font(R.font.poppins_light))
                            )
                        }
                    }
                }

                IconButton(onClick = {
                    val noteDB = Note(id = 0, title = title, note = description)
                    viewModel.insertNote(noteDB)
                    navigateBack()

                }) {
                    Icon(
                        painterResource(R.drawable.save),
                        contentDescription = "save"
                    )
                }
            }
        }
    )
}
