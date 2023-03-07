package com.vangelnum.newsapp.feature_favourite.di

import android.content.Context
import androidx.room.Room
import com.vangelnum.newsapp.feature_favourite.data.network.FavouriteDao
import com.vangelnum.newsapp.feature_favourite.data.network.FavouriteDatabase
import com.vangelnum.newsapp.feature_favourite.data.repository.FavouriteRepositoryImpl
import com.vangelnum.newsapp.feature_favourite.domain.repository.FavouriteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FavouriteModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): FavouriteDatabase {
        synchronized(this) {
            return Room.databaseBuilder(
                context,
                FavouriteDatabase::class.java,
                "database")
                .fallbackToDestructiveMigration()
                .build()
        }
    }

    @Provides
    @Singleton
    fun provideDao(appDatabase: FavouriteDatabase): FavouriteDao {
        return appDatabase.getDao()
    }

    @Singleton
    @Provides
    fun provideRepository(myDao: FavouriteDao): FavouriteRepository {
        return FavouriteRepositoryImpl(myDao)
    }
}