package com.aritra.notify.domain.repository.trash

import com.aritra.notify.data.dao.TrashNoteDao
import com.aritra.notify.domain.models.TrashNoteWithNotes
import com.aritra.notify.domain.models.TrashNote
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import javax.inject.Inject

class TrashNoteRepoImpl @Inject constructor(
    private val noteTrashDao: TrashNoteDao,
) : TrashNoteRepo {

    override suspend fun getTrashNotes(): List<TrashNote> {
        return noteTrashDao.getTrashNote()
    }

    override suspend fun deleteTrashNoteById(noteId: Int) {
        noteTrashDao.deleteTrashNoteById(noteId)
    }

    override suspend fun upsertTrashNote(trashNote: TrashNote) {
        noteTrashDao.upsertTrashNote(trashNote)
    }

    override suspend fun getTrashNoteWithNote(): List<TrashNoteWithNotes> {
        return noteTrashDao.getTrashNoteWithNote()
    }

    override suspend fun getTrashNotePeriodHasExceeded(localDateTime: LocalDateTime): List<Int> {
        val getTrashNote = getTrashNotes()
        return getTrashNote.filter {
            val dateTime = it.dateTime
            dateTime.until(localDateTime, ChronoUnit.DAYS) >= 28
        }.map {
            it.noteId
        }
    }
}
