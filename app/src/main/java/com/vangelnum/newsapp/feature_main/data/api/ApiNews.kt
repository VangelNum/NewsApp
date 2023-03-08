package com.vangelnum.newsapp.feature_main.data.api

import com.vangelnum.newsapp.core.data.dto.NewsDto
import com.vangelnum.newsapp.core.utils.Constants.API_KEY
import retrofit2.http.GET

interface ApiNews {
    @GET("/v2/top-headlines?country=us&apiKey=$API_KEY")
    suspend fun getNews(): NewsDto
}