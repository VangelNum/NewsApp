package com.vangelnum.newsapp

import com.vangelnum.newsapp.Constants.API_KEY
import com.vangelnum.newsapp.data.News
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MyApi {
    @GET("/v2/top-headlines?country=ru&apiKey=$API_KEY")
    suspend fun getNews(): Response<News>

    @GET("v2/everything?apiKey=$API_KEY")
    suspend fun getSearchNews(
        @Query("q") query: String,
        @Query("sortBy") sortBy: String,
        @Query("from") from: String?,
        @Query("to") to: String?,
    ): Response<News>

}