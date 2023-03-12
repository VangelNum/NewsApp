package com.vangelnum.newsapp.feature_search.domain.repository

import com.vangelnum.newsapp.core.domain.model.News
import com.vangelnum.newsapp.feature_search.data.common.SearchResource
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    suspend fun getSearchNews(
        query: String,
        sortBy: String,
        from: String?,
        to: String?,
    ): Flow<SearchResource<News>>
}