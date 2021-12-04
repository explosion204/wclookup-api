package com.example.wclookup.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.wclookup.ui.viewmodel.factory.ViewModelFactory
import dagger.Module
import dagger.Provides
import javax.inject.Provider
import javax.inject.Singleton

@Module
class ViewModelFactoryModule {
    @Provides
    @Singleton
    fun provideViewModelFactory(providerMap: Map<Class<out ViewModel>, Provider<ViewModel>>):
            ViewModelProvider.Factory {
        return ViewModelFactory(providerMap)
    }
}