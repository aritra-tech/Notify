@file:OptIn(ExperimentalMaterial3Api::class)

package com.aritra.notify.components.topbar

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import com.aritra.notify.R

@Preview
@Composable
fun TopBar() {
    TopAppBar(
        title = {
            Text(
                text = "All Notes",
                fontFamily = FontFamily(Font(R.font.poppins_medium))
            )
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.Transparent,
        )
    )
}