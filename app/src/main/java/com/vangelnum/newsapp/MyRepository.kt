package com.vangelnum.newsapp

import com.vangelnum.newsapp.data.News
import retrofit2.Response

interface MyRepository {
    suspend fun getNews(): Response<News>
    suspend fun getSearchNews(query: String): Response<News>
}