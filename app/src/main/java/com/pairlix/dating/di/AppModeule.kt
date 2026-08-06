package com.pairlix.dating.di

import android.content.Context
import com.pairlix.dating.utils.BillingManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideBillingManager(
        @ApplicationContext context: Context
    ): BillingManager = BillingManager(context)



}