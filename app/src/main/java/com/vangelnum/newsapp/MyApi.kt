package com.vangelnum.newsapp

import com.vangelnum.newsapp.data.News
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MyApi {
    @GET("/v2/top-headlines?country=ru&apiKey=7badb69b2e6c49309030ed889856c24b")
    suspend fun getNews(): Response<News>

    @GET("v2/everything/?country-ru&apiKey=7badb69b2e6c49309030ed889856c24b")
    suspend fun getSearchNews(
        @Query("q") query: String
    ): Response<News>
}