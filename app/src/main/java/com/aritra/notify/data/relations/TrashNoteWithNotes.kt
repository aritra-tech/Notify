package com.aritra.notify.data.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.aritra.notify.domain.models.Note
import com.aritra.notify.domain.models.TrashNote

data class TrashNoteWithNotes(
    @Embedded val trashNote : TrashNote,
    @Relation(
        parentColumn = "noteId",
        entityColumn = "id"
    )
    val note:Note
)
