package com.vangelnum.newsapp.feature_favourite.data.network

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vangelnum.newsapp.feature_favourite.data.model.FavouriteData

@Database(entities = [FavouriteData::class], version = 3)
abstract class FavouriteDatabase : RoomDatabase() {
    abstract fun getDao(): FavouriteDao
}