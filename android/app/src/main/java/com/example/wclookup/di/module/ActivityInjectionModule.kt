package com.example.wclookup.di.module

import com.example.wclookup.ui.activity.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityInjectionModule {
    @ContributesAndroidInjector(modules = [ViewModelModule::class])
    abstract fun contributesInjectMainActivity(): MainActivity
}