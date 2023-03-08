package com.vangelnum.newsapp.feature_search.di

import com.vangelnum.newsapp.core.utils.Constants
import com.vangelnum.newsapp.feature_search.data.api.ApiSearch
import com.vangelnum.newsapp.feature_search.data.repository.SearchRepositoryImpl
import com.vangelnum.newsapp.feature_search.domain.repository.SearchRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SearchModule {


    @Singleton
    @Provides
    fun provideSearchApi(): ApiSearch {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiSearch::class.java)
    }

    @Singleton
    @Provides
    fun provideSearchRepository(api: ApiSearch): SearchRepository {
        return SearchRepositoryImpl(api)
    }
}