package com.aritradas.notify.di

import com.aritradas.notify.services.DispatcherProvider
import com.aritradas.notify.services.DispatcherProviderImpl
import com.aritradas.notify.services.alarm.AlarmScheduler
import com.aritradas.notify.services.alarm.AlarmSchedulerImpl
import com.aritradas.notify.domain.repository.trash.TrashNoteRepoImpl
import com.aritradas.notify.domain.repository.trash.TrashNoteRepo
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DispatcherModule {

    @Binds
    abstract fun bindDispatcherProvider(
        dispatcherProvider: DispatcherProviderImpl,
    ): DispatcherProvider

    @Binds
    abstract fun bindTrashRepo(
        trashNoteRepository: TrashNoteRepoImpl,
    ): TrashNoteRepo

    @Binds
    abstract fun bindAlarm(
        trashNoteRepository: AlarmSchedulerImpl,
    ): AlarmScheduler
}
