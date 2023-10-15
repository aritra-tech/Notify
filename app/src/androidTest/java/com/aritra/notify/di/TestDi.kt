package com.aritra.notify.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.aritra.notify.data.db.NoteDatabase
import com.aritra.notify.domain.repository.NoteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(components = [SingletonComponent::class], replaces = [AppModule::class])
object TestDi {


    @Provides
    fun provideContext(@ApplicationContext context: Context): Context {
        return context
    }

    @Provides
    fun provideDataStoreUtil(@ApplicationContext context: Context): DataStoreUtil = DataStoreUtil(context)

    @Provides
    fun provideRepository(application: Application): NoteRepository {
        return NoteRepository(application)
    }

    @Provides
    @Singleton

    fun provideDatabase(@ApplicationContext context: Context): NoteDatabase {
        return Room.inMemoryDatabaseBuilder(
            context.applicationContext,
            NoteDatabase::class.java,
        )
            .build()
    }

    @Provides
    @Singleton
    fun provideTrashNote(noteDatabase: NoteDatabase) = noteDatabase.trashNote
}