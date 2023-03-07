package com.vangelnum.newsapp.feature_main.domain.repository

import com.vangelnum.newsapp.core.data.dto.News
import retrofit2.Response

interface MainRepository {
    suspend fun getNews(): Response<News>
}