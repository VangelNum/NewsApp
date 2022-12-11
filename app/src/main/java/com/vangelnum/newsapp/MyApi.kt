package com.vangelnum.newsapp

import com.vangelnum.newsapp.data.News
import retrofit2.Response
import retrofit2.http.GET

interface MyApi {
    @GET("/v2/top-headlines?country=us&apiKey=7badb69b2e6c49309030ed889856c24b")
    suspend fun getNews(): Response<News>
}