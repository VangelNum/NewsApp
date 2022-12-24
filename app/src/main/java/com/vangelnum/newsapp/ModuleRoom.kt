package com.vangelnum.newsapp

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ModuleRoom {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): RoomDatabase {
        synchronized(this) {
            return Room.databaseBuilder(
                context,
                RoomDatabase::class.java,
                "database")
                .fallbackToDestructiveMigration()
                .build()
        }
    }

    @Provides
    @Singleton
    fun provideDao(appDatabase: RoomDatabase): RoomDao {
        return appDatabase.getDao()
    }

    @Singleton
    @Provides
    fun provideRepository(myDao: RoomDao): RoomRepository {
        return RoomRepositoryImpl(myDao)
    }
}