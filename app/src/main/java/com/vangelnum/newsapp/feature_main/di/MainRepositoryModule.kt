package com.vangelnum.newsapp.feature_main.di

import com.vangelnum.newsapp.feature_main.data.repository.MainRepositoryImpl
import com.vangelnum.newsapp.feature_main.domain.repository.MainRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class MainRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindMainRepository(
        mainRepositoryImpl: MainRepositoryImpl
    ): MainRepository
}