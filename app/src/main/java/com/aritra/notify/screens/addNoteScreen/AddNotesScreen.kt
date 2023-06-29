

package com.aritra.notify.screens.addNoteScreen


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aritra.notify.R
import com.aritra.notify.components.AddNoteTopBar

@Composable
fun AddNotesScreen(
    navigateBack: () -> Unit
) {
    val viewModel : AddNoteViewModel = viewModel()
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
//    val focus = LocalFocusManager.current
    Scaffold(
        topBar = { AddNoteTopBar(viewModel,navigateBack,title,description)},
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
                    onValueChange = { title = it },
                    placeholder = { Text(
                        "Title",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.W700
                    )},
                    textStyle = TextStyle(
                        fontFamily = FontFamily(Font(R.font.poppins_medium)),
                    ),
                    maxLines = Int.MAX_VALUE
                )
                TextField(
                    value = description,
                    onValueChange = { description = it },
                    placeholder = { Text(
                        "Notes",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.W500
                    )},
                    textStyle = TextStyle(
                        fontFamily = FontFamily(Font(R.font.poppins_light)),
                    ),
                    modifier = Modifier.fillMaxSize(),

                )
            }
        }
    }
}