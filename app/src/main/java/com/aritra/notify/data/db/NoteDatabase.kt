package com.aritra.notify.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.aritra.notify.data.converters.CollectionConverter
import com.aritra.notify.data.converters.DateTypeConverter
import com.aritra.notify.data.converters.LocalDateTimeConverter
import com.aritra.notify.data.converters.UriConverter
import com.aritra.notify.data.dao.NoteDao
import com.aritra.notify.data.dao.TrashNoteDao
import com.aritra.notify.domain.models.Note
import com.aritra.notify.domain.models.TrashNote

@Database(entities = [Note::class, TrashNote::class], version = 6)
@TypeConverters(
    DateTypeConverter::class,
    UriConverter::class,
    CollectionConverter::class,
    LocalDateTimeConverter::class
)
abstract class NoteDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao
    abstract val trashNote: TrashNoteDao

    companion object {
        @Suppress("ktlint:standard:property-naming")
        private var INSTANCE: NoteDatabase? = null
        fun getInstance(context: Context): NoteDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        NoteDatabase::class.java,
                        "Note_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}
