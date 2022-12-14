package com.vangelnum.newsapp

import androidx.lifecycle.LiveData
import com.vangelnum.newsapp.data.News

interface RoomRepository {
    fun getAll() : LiveData<List<RoomEntity>>

    suspend fun addNews(news: RoomEntity)

    suspend fun deleteNews(news: RoomEntity)

}