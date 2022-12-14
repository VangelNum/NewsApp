package com.vangelnum.newsapp

import androidx.lifecycle.LiveData
import javax.inject.Inject

class RoomRepositoryImpl @Inject constructor(
    private val myDao: RoomDao,
) : RoomRepository {
    override fun getAll(): LiveData<List<RoomEntity>> {
        return myDao.getAll()
    }
    override suspend fun addNews(news: RoomEntity) {
        return myDao.addNews(news)
    }

    override suspend fun deleteNews(news: RoomEntity) {
        return myDao.deleteNews(news)
    }
}