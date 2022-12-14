package com.vangelnum.newsapp

import com.vangelnum.newsapp.data.News
import retrofit2.Response

class MyRepositoryImpl(
    private val api: MyApi,
) : MyRepository {
    override suspend fun getNews(): Response<News> {
        return api.getNews()
    }

    override suspend fun getSearchNews(
        query: String,
        sortBy: String,
        from: String?,
        to: String?,
    ): Response<News> {
        return api.getSearchNews(query, sortBy, from, to)
    }
}