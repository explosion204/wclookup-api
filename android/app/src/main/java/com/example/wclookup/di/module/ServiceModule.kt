package com.example.wclookup.di.module

import com.example.wclookup.core.service.*
import com.example.wclookup.core.service.impl.*
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ServiceModule {
    @Provides
    @Singleton
    fun provideToiletService(): ToiletService = ToiletServiceImpl()

    @Provides
    @Singleton
    fun provideAuthService(): AuthService = AuthServiceImpl()

    @Provides
    @Singleton
    fun provideUserService(): UserService = UserServiceImpl()

    @Provides
    @Singleton
    fun provideReviewService(): ReviewService = ReviewServiceImpl()

    @Provides
    @Singleton
    fun provideTicketService(): TicketService = TicketServiceImpl()
}