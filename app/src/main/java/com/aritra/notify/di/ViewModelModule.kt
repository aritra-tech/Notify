package com.aritra.notify.di

import com.aritra.notify.components.biometric.AppBioMetricManager
import com.aritra.notify.viewmodel.MainViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class ViewModelModule {

    @Provides
    fun provideMainViewModel(
        bioMetricManager: AppBioMetricManager,
        dataStoreUtil: DataStoreUtil,
    ): MainViewModel {
        return MainViewModel(bioMetricManager, dataStoreUtil)
    }
}
