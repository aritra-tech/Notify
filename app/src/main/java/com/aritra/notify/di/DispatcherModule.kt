package com.aritra.notify.di

import com.aritra.notify.core.dispatcher.DispatcherProvider
import com.aritra.notify.core.dispatcher.DispatcherProviderImpl
import com.aritra.notify.domain.repository.trash.TrashNoteRepository
import com.aritra.notify.domain.repository.trash.TrashRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DispatcherModule {

    @Binds
    abstract fun bindDispatcherProvider(
        dispatcherProvider: DispatcherProviderImpl
    ):DispatcherProvider

    @Binds
    abstract fun bindTrashRepo(
        trashNoteRepository: TrashNoteRepository
    ):TrashRepository
}