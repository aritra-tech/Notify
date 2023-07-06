package com.aritra.notify.components

import androidx.compose.foundation.clickable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import com.aritra.notify.R

@Composable
fun ShareOption(
    text: String,
    onClick: () -> Unit
) {
    Text(
        text = text,
        fontSize = 18.sp,
        fontFamily = FontFamily(Font(R.font.poppins_medium)),
        modifier = Modifier.clickable { onClick() }
    )
}