package com.example.wclookup.di.module

import androidx.lifecycle.ViewModel
import com.example.wclookup.di.annotation.ViewModelKey
import com.example.wclookup.ui.viewmodel.*
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(AuthViewModel::class)
    abstract fun bindAuthViewModel(authViewModel: AuthViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MapsViewModel::class)
    abstract fun bindMapsViewModel(mapsViewModel: MapsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ToiletViewModel::class)
    abstract fun bindToiletViewModel(toiletViewModel: ToiletViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TicketViewModel::class)
    abstract fun bindTicketViewModel(ticketViewModel: TicketViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ReviewViewModel::class)
    abstract fun bindReviewViewModel(reviewViewModel: ReviewViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FavouritesViewModel::class)
    abstract fun bindFavouritesViewModel(favouritesViewModel: FavouritesViewModel): ViewModel
}