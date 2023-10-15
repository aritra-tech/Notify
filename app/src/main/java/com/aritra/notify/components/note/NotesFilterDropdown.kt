package com.aritra.notify.components.note

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aritra.notify.utils.NotesFilter
import com.aritra.notify.utils.OrderType.Ascending
import com.aritra.notify.utils.OrderType.Descending

@Composable
fun NotesFilterDropdown(
    modifier: Modifier,
    notesFilter: NotesFilter = NotesFilter.Date(orderType = Descending),
    onFilterChange: (NotesFilter) -> Unit,
) {
    Column(modifier = modifier) {
        Row(modifier = Modifier.fillMaxWidth()) {
            // Date
            SelectableRadioButton(
                title = "Date",
                isSelected = notesFilter is NotesFilter.Date,
                onClick = {
                    onFilterChange(NotesFilter.Date(orderType = notesFilter.orderType))
                }
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Title
            SelectableRadioButton(
                title = "Title",
                isSelected = notesFilter is NotesFilter.Title,
                onClick = {
                    onFilterChange(NotesFilter.Title(orderType = notesFilter.orderType))
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            SelectableRadioButton(
                title = if (notesFilter is NotesFilter.Date) "Oldest" else "A - Z",
                isSelected = notesFilter.orderType is Ascending,
                onClick = { onFilterChange(notesFilter.copy(orderType = Ascending)) }
            )
            Spacer(Modifier.width(16.dp))
            SelectableRadioButton(
                title = if (notesFilter is NotesFilter.Date) "Newest" else "Z - A",
                isSelected = notesFilter.orderType is Descending,
                onClick = { onFilterChange(notesFilter.copy(orderType = Descending)) }
            )
        }
    }
}

@Composable
fun SelectableRadioButton(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
    ) {
        RadioButton(selected = isSelected, onClick = onClick)
        Text(text = title)
    }
}
