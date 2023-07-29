@file:OptIn(ExperimentalMaterial3Api::class)

package com.aritra.notify.components.topbar

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aritra.notify.R

@Preview
@Composable
fun TopBar(
    title: String = stringResource(R.string.demo_text)
) {
    TopAppBar(
        title = {
            Text(
                modifier = Modifier.padding(top = 10.dp),
                text = title,
                fontSize = 28.sp,
                fontFamily = FontFamily(Font(R.font.poppins_semibold))
            )
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.Transparent,
        )
    )
}