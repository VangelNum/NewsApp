package com.vangelnum.newsapp.feature_main.di

import com.vangelnum.newsapp.core.utils.Constants.BASE_URL
import com.vangelnum.newsapp.feature_main.data.api.ApiNews
import com.vangelnum.newsapp.feature_main.domain.repository.MainRepository
import com.vangelnum.newsapp.feature_main.data.repository.MainRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MainModule {

    @Singleton
    @Provides
    fun provideMyApi(): ApiNews {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiNews::class.java)
    }

    @Singleton
    @Provides
    fun provideMyRepository(api: ApiNews): MainRepository {
        return MainRepositoryImpl(api)
    }
}