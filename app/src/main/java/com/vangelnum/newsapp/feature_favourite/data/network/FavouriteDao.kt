package com.vangelnum.newsapp.feature_favourite.data.network

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vangelnum.newsapp.feature_favourite.data.model.FavouriteData
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouriteDao {

    @Query("SELECT * FROM room_table")
    fun getAll(): Flow<List<FavouriteData>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addNews(itemNews: FavouriteData)

    @Delete
    suspend fun deleteNews(itemNews: FavouriteData)

}