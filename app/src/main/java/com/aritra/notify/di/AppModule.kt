package com.aritra.notify.di

import android.app.Application
import android.content.Context
import com.aritra.notify.data.repository.NoteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun provideContext(@ApplicationContext context: Context): Context {
        return context
    }

    @Provides
    fun provideDataStoreUtil(@ApplicationContext context: Context):DataStoreUtil = DataStoreUtil(context)

    @Provides
    fun provideRepository(application: Application): NoteRepository {
        return NoteRepository(application)
    }
}