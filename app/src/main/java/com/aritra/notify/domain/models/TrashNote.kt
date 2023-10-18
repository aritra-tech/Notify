package com.aritra.notify.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity
data class TrashNote(
    @PrimaryKey(autoGenerate = true)
    val noteId: Int = 0,
    val dateTime: LocalDateTime = LocalDateTime.now(),
)
