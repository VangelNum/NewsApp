package com.vangelnum.newsapp.feature_favourite.data.repository

import com.vangelnum.newsapp.core.common.Resource
import com.vangelnum.newsapp.feature_favourite.data.model.FavouriteData
import com.vangelnum.newsapp.feature_favourite.data.network.FavouriteDao
import com.vangelnum.newsapp.feature_favourite.domain.repository.FavouriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FavouriteRepositoryImpl @Inject constructor(
    private val myDao: FavouriteDao,
) : FavouriteRepository {
    override fun getAll(): Flow<Resource<List<FavouriteData>>> = flow {
        try {
            myDao.getAll().collect {
                emit(Resource.Success(it))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.Error(e.message.toString()))
        }
    }

    override suspend fun addNews(news: FavouriteData) {
        return myDao.addNews(news)
    }

    override suspend fun deleteNews(news: FavouriteData) {
        return myDao.deleteNews(news)
    }
}