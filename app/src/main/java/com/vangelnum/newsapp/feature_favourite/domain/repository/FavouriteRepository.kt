package com.vangelnum.newsapp.feature_favourite.domain.repository

import androidx.lifecycle.LiveData
import com.vangelnum.newsapp.feature_favourite.data.model.FavouriteData

interface FavouriteRepository {
    fun getAll(): LiveData<List<FavouriteData>>

    suspend fun addNews(news: FavouriteData)

    suspend fun deleteNews(news: FavouriteData)

}