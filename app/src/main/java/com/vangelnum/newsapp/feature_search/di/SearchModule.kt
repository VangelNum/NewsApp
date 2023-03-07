package com.vangelnum.newsapp.feature_search.di

import com.vangelnum.newsapp.feature_main.data.api.ApiNews
import com.vangelnum.newsapp.feature_search.data.SearchRepositoryImpl
import com.vangelnum.newsapp.feature_search.domain.repository.SearchRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SearchModule {
    @Singleton
    @Provides
    fun provideSearchRepository(api: ApiNews): SearchRepository {
        return SearchRepositoryImpl(api)
    }
}