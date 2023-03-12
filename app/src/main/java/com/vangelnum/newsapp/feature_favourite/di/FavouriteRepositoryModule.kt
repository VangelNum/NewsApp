package com.vangelnum.newsapp.feature_favourite.di

import com.vangelnum.newsapp.feature_favourite.data.repository.FavouriteRepositoryImpl
import com.vangelnum.newsapp.feature_favourite.domain.repository.FavouriteRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class FavouriteRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindsFavouriteRepository(
        favouriteRepository: FavouriteRepositoryImpl
    ): FavouriteRepository
}