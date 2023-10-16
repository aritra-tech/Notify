package com.aritra.notify.repo

import com.aritra.notify.data.dao.TrashNoteDao
import com.aritra.notify.domain.models.TrashNote
import com.aritra.notify.domain.repository.trash.TrashNoteRepository
import com.aritra.notify.domain.repository.trash.TrashRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.LocalDateTime
import javax.inject.Inject

@HiltAndroidTest
class TrashNoteRepositoryTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var trashNoteDao: TrashNoteDao

    lateinit var trashRepository: TrashRepository

    @Before
    fun setUp(){
        hiltRule.inject()
        runTest {
            (0..10).forEach {
                trashNoteDao.upsertTrashNote(TrashNote(it, LocalDateTime.now()))
            }
            trashNoteDao.upsertTrashNote(TrashNote(10, LocalDateTime.now().plusDays(29)))
            trashNoteDao.upsertTrashNote(TrashNote(0, LocalDateTime.now()))

        }
        trashRepository = TrashNoteRepository(trashNoteDao)
    }

    @Test
    fun checkTrashNote(){
        runTest {
            val getTrashNote = trashNoteDao.getTrashNote()
            assert(getTrashNote.size == 11)
        }
    }

    @Test
    fun checkIfTrashNotePeriodHasExceeded(){
        runTest {
            val getNotes = trashRepository.getTrashNotePeriodHasExceeded()
            assert(getNotes.size ==1)
            assert(getNotes[0] == 10)

        }
    }

    @Test
    fun checkNoteTrashPeriodHas5Exceeded(){
        runTest {
            trashRepository.deleteTrashNoteById(10)
            trashNoteDao.upsertTrashNote(TrashNote(20, LocalDateTime.now()))
            trashNoteDao.upsertTrashNote(TrashNote(21, LocalDateTime.now()))
            trashNoteDao.upsertTrashNote(TrashNote(0, LocalDateTime.now()))
            trashNoteDao.upsertTrashNote(TrashNote(22, LocalDateTime.now()))
            trashNoteDao.upsertTrashNote(TrashNote(23, LocalDateTime.now()))
            trashNoteDao.upsertTrashNote(TrashNote(24, LocalDateTime.now()))
            val getTrashNote = trashRepository.getTrashNotePeriodHasExceeded(LocalDateTime.now().plusDays(29))
            println(getTrashNote)
//            assert(getTrashNote.size ==5)
            assert(getTrashNote.containsAll(listOf(20,21,22,23,24)))
        }
    }
}