package com.vangelnum.newsapp.feature_main.data.api

import com.vangelnum.newsapp.core.utils.Constants.API_KEY
import com.vangelnum.newsapp.core.data.dto.News
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiNews {
    @GET("/v2/top-headlines?country=us&apiKey=$API_KEY")
    suspend fun getNews(): Response<News>

    @GET("v2/everything?apiKey=$API_KEY")
    suspend fun getSearchNews(
        @Query("q") query: String,
        @Query("sortBy") sortBy: String,
        @Query("from") from: String?,
        @Query("to") to: String?,
    ): Response<News>

}