package com.vangelnum.newsapp

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [RoomEntity::class], version = 2)
abstract class RoomDatabase : RoomDatabase() {
    abstract fun getDao(): RoomDao
}