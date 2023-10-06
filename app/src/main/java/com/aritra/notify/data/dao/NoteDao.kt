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

    @Query("SELECT * FROM note WHERE id = :noteId")
    fun getNoteById(noteId: Int): Flow<Note>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(noteModel: Note): Long

    @Update
    suspend fun updateNote(noteModel: Note)

    @Delete
    suspend fun deleteNote(noteModel: Note)

    @Query("DELETE FROM note")
    suspend fun clear()
}