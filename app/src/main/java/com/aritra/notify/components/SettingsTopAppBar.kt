package com.aritra.notify.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import com.aritra.notify.R

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun SettingsTopAppBar() {
    TopAppBar(
        title = {
            Text(
                text = "Settings",
                fontFamily = FontFamily(Font(R.font.poppins_medium))
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.onSecondary
        )
    )
}