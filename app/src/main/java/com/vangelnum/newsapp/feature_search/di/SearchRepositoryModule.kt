package com.vangelnum.newsapp.feature_search.di

import com.vangelnum.newsapp.feature_search.data.repository.SearchRepositoryImpl
import com.vangelnum.newsapp.feature_search.domain.repository.SearchRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SearchRepositoryModule {
    @Binds
    @Singleton
    abstract fun provideSearchRepository(
        searchRepositoryImpl: SearchRepositoryImpl
    ): SearchRepository
}