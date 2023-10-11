package com.aritra.notify.components.actions

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable

@Composable
fun FilterNotesButton(
    onFilterClick: () -> Unit,
) {
    IconButton(onClick = onFilterClick) {
        Icon(
            imageVector = Icons.Default.FilterAlt,
            contentDescription = "Filter"
        )
    }
}
