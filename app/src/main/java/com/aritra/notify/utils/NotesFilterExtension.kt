package com.aritra.notify.utils

import com.aritra.notify.domain.models.Note

fun List<Note>.filterNotes(notesFilter: NotesFilter): List<Note> {
    return when (notesFilter.orderType) {
        is OrderType.Ascending -> {
            when (notesFilter) {
                is NotesFilter.Date -> this.sortedBy { it.dateTime }
                is NotesFilter.Title -> this.sortedBy { it.title }
            }
        }

        is OrderType.Descending -> {
            when (notesFilter) {
                is NotesFilter.Date -> this.sortedByDescending { it.dateTime }
                is NotesFilter.Title -> this.sortedByDescending { it.title }
            }
        }

        else -> this.sortedByDescending { it.dateTime }
    }
}
