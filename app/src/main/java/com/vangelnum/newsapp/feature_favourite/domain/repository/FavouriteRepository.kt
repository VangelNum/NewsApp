package com.vangelnum.newsapp.feature_favourite.domain.repository

import com.vangelnum.newsapp.core.common.Resource
import com.vangelnum.newsapp.feature_favourite.data.model.FavouriteData
import kotlinx.coroutines.flow.Flow

interface FavouriteRepository {
    fun getAll(): Flow<Resource<List<FavouriteData>>>

    suspend fun addNews(news: FavouriteData)

    suspend fun deleteNews(news: FavouriteData)

}