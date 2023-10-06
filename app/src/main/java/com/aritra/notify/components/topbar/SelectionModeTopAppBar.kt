package com.aritra.notify.components.topbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectionModeTopAppBar(selectedItems: List<Int>, onDeleteClick:()->Unit, resetSelectionMode: () -> Unit) {



    TopAppBar(  navigationIcon = {
        IconButton(
            onClick = resetSelectionMode,
        ) {
            Icon(
                imageVector = Icons.Rounded.Close,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground,
            )
        }
    },title = {
        Text(
            text = "${selectedItems.size} selected",
            style = MaterialTheme.typography.titleMedium.copy(
                color = MaterialTheme.colorScheme.onBackground,
            ),
        )

    }, actions = {
        IconButton(onDeleteClick){
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground,
            )
        }
    })


    /*Row(modifier = Modifier.fillMaxWidth().padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = Icons.Rounded.Close,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.clickable {
                resetSelectionMode()
            }
        )
        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = "${selectedItems.size} selected",
            style = MaterialTheme.typography.titleMedium.copy(
                color = MaterialTheme.colorScheme.onBackground,
            ),
        )

        Spacer(modifier = Modifier.weight(1f))

        Icon(
            imageVector = Icons.Filled.Delete,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.clickable {
                onDeleteClick()
            }
        )
    }*/
}


/*
@Preview(showBackground = true)
@Composable
fun Preview() {
    SelectionModeTopAppBar(selectedItems = emptyList()) {

    }
}
*/
