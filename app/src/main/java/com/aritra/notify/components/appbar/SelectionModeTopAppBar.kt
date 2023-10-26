package com.aritra.notify.components.appbar

import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.aritra.notify.R

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
            modifier = Modifier.padding(start = 80.dp),
            text = "${selectedItems.size} item selected",
            style = MaterialTheme.typography.titleMedium.copy(
                color = MaterialTheme.colorScheme.onBackground,
                fontFamily = FontFamily(Font(R.font.poppins_medium))
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
