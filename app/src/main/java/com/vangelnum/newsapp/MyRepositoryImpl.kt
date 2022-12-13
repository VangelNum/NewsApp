package com.vangelnum.newsapp

import com.vangelnum.newsapp.data.News
import retrofit2.Response

class MyRepositoryImpl(
    private val api: MyApi,
) : MyRepository {
    override suspend fun getNews(): Response<News> {
        return api.getNews()
    }
}