package com.aritra.notify.di

import android.app.NotificationManager
import android.content.Context
import androidx.room.Room
import com.aritra.notify.data.db.NoteDatabase
import com.aritra.notify.utils.DataStoreUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun provideContext(@ApplicationContext context: Context): Context {
        return context
    }

    @Provides
    fun provideDataStoreUtil(@ApplicationContext context: Context): DataStoreUtil = DataStoreUtil(context)

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): NoteDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            NoteDatabase::class.java,
            "Note_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideTrashNote(noteDatabase: NoteDatabase) = noteDatabase.trashNote

    @Provides
    @Singleton
    fun providerNotificationManager(@ApplicationContext context: Context) =
        context.getSystemService(NotificationManager::class.java)
}
