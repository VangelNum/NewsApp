package com.vangelnum.newsapp.feature_main.data.repository

import com.vangelnum.newsapp.core.data.dto.News
import com.vangelnum.newsapp.feature_main.data.api.ApiNews
import com.vangelnum.newsapp.feature_main.domain.repository.MainRepository
import retrofit2.Response

class MainRepositoryImpl(
    private val api: ApiNews,
) : MainRepository {
    override suspend fun getNews(): Response<News> {
        return api.getNews()
    }
}