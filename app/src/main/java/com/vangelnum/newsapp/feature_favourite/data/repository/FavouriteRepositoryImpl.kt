package com.vangelnum.newsapp.feature_favourite.data.repository

import androidx.lifecycle.LiveData
import com.vangelnum.newsapp.feature_favourite.data.network.FavouriteDao
import com.vangelnum.newsapp.feature_favourite.data.model.FavouriteData
import com.vangelnum.newsapp.feature_favourite.domain.repository.FavouriteRepository
import javax.inject.Inject

class FavouriteRepositoryImpl @Inject constructor(
    private val myDao: FavouriteDao,
) : FavouriteRepository {
    override fun getAll(): LiveData<List<FavouriteData>> {
        return myDao.getAll()
    }

    override suspend fun addNews(news: FavouriteData) {
        return myDao.addNews(news)
    }

    override suspend fun deleteNews(news: FavouriteData) {
        return myDao.deleteNews(news)
    }
}