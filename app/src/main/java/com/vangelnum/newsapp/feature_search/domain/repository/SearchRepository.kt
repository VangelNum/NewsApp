package com.vangelnum.newsapp.feature_search.domain.repository

import com.vangelnum.newsapp.core.data.dto.News
import retrofit2.Response

interface SearchRepository {
    suspend fun getSearchNews(
        query: String,
        sortBy: String,
        from: String?,
        to: String?,
    ): Response<News>
}