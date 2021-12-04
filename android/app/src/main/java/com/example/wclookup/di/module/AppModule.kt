package com.example.wclookup.di.module

import android.app.Application
import android.content.SharedPreferences
import com.example.wclookup.core.constant.NameConstant.APP_PREFERENCES
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {
    @Singleton
    @Provides
    fun provideSharedPreferences(application: Application): SharedPreferences {
        return application.getSharedPreferences(APP_PREFERENCES, Application.MODE_PRIVATE)
    }
}