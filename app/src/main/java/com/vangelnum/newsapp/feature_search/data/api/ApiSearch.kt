package com.vangelnum.newsapp.feature_search.data.api

import com.vangelnum.newsapp.core.data.dto.NewsDto
import com.vangelnum.newsapp.core.utils.Constants.API_KEY
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiSearch {
    @GET("v2/everything?apiKey=$API_KEY")
    suspend fun getSearchNews(
        @Query("q") query: String,
        @Query("sortBy") sortBy: String,
        @Query("from") from: String?,
        @Query("to") to: String?,
    ): NewsDto
}