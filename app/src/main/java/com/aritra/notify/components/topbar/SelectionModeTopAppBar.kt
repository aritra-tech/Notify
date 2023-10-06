package com.aritra.notify.components.topbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectionModeTopAppBar(selectedItems: List<Int>, onDeleteClick: () -> Unit, resetSelectionMode: () -> Unit) {
    TopAppBar(navigationIcon = {
        IconButton(
            onClick = resetSelectionMode
        ) {
            Icon(
                imageVector = Icons.Outlined.Close,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    }, title = {
        Text(
            text = "${selectedItems.size} selected",
            style = MaterialTheme.typography.titleMedium.copy(
                color = MaterialTheme.colorScheme.onBackground
            )
        )
    }, actions = {
        IconButton(onDeleteClick) {
            Icon(
                imageVector = Icons.Outlined.DeleteOutline,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    })
}