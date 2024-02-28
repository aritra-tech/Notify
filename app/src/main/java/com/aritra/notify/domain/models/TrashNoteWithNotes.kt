package com.aritra.notify.domain.models

import androidx.room.Embedded
import androidx.room.Relation

data class TrashNoteWithNotes(
    @Embedded val trashNote: TrashNote,
    @Relation(
        parentColumn = "noteId",
        entityColumn = "id"
    )
    val note: Note,
)
