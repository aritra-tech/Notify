package com.aritra.notify.components.bottombar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.material.icons.outlined.RemoveCircleOutline
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.aritra.notify.domain.models.Note

@Composable
fun SelectedModeBottomBar(selectedNotes: List<Note>, onDeleteClick: () -> Unit, onPinNote: (isAlreadyPinned:Boolean) -> Unit) {
    BottomAppBar(actions = {
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = { onDeleteClick.invoke() }) {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = null
                )
            }
            if (selectedNotes.size == 1) {
                if (selectedNotes[0].isPinned) {
                    IconButton(onClick = { onPinNote.invoke(true) }) {
                        Icon(
                            imageVector = Icons.Outlined.RemoveCircleOutline,
                            contentDescription = null
                        )
                    }
                } else {
                    IconButton(onClick = { onPinNote.invoke(false) }) {
                        Icon(
                            imageVector = Icons.Outlined.PushPin,
                            contentDescription = null
                        )
                    }
                }
            }
        }
    })
}