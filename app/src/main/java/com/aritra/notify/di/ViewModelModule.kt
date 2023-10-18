package com.aritra.notify.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class ViewModelModule {

//    @Provides
//    fun provideMainViewModel(
//        bioMetricManager: AppBioMetricManager,
//        dataStoreUtil: DataStoreUtil,
//    ): MainViewModel {
//        return MainViewModel(bioMetricManager, dataStoreUtil)
//    }
}
