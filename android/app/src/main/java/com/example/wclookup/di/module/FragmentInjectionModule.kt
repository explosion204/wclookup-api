package com.example.wclookup.di.module

import com.example.wclookup.ui.fragment.AuthFragment
import com.example.wclookup.ui.fragment.MapsFragment
import com.example.wclookup.ui.fragment.ToiletFragment
import com.example.wclookup.ui.fragment.ToiletsInRadiusFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentInjectionModule {
    @ContributesAndroidInjector(modules = [ViewModelModule::class])
    abstract fun contributesInjectAuthFragment(): AuthFragment

    @ContributesAndroidInjector(modules = [ViewModelModule::class])
    abstract fun contributesInjectMapsFragment(): MapsFragment

    @ContributesAndroidInjector(modules = [ViewModelModule::class])
    abstract fun contributesInjectToiletsInRadiusFragment(): ToiletsInRadiusFragment

    @ContributesAndroidInjector(modules = [ViewModelModule::class])
    abstract fun contributesInjectToiletFragment(): ToiletFragment
}