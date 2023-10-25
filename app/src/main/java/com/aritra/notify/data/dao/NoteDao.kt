package com.aritra.notify.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.aritra.notify.domain.models.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Query("SELECT * FROM note ORDER BY dateTime DESC")
    fun getAllNotes(): Flow<List<Note>>

    @Query("DELETE FROM note WHERE id = :noteId")
    suspend fun deleteNoteById(noteId: Int)

    @Query("SELECT * FROM note WHERE id = :noteId")
    fun getNoteByIdFlow(noteId: Int): Flow<Note?>

    @Query("SELECT * FROM note WHERE id = :noteId")
    fun getNoteById(noteId: Int): Note?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(noteModel: Note): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListOfNotes(noteModels: List<Note>): List<Long>

    @Update
    suspend fun updateNote(noteModel: Note)

    @Delete
    suspend fun deleteNote(noteModel: Note)

    @Delete
    suspend fun deleteListOfNote(noteModel: List<Note>)

    @Query("DELETE FROM note")
    suspend fun clear()
}
