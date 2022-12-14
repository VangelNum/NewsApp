package com.vangelnum.newsapp

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query

@Dao
interface RoomDao {

    @Query("SELECT * FROM room_table")
    fun getAll(): LiveData<List<RoomEntity>>

    @Insert(onConflict = REPLACE)
    suspend fun addNews(itemNews: RoomEntity)

    @Delete
    suspend fun deleteNews(itemNews: RoomEntity)

}