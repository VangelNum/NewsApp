package com.vangelnum.newsapp.feature_search.domain.repository

import com.vangelnum.newsapp.core.common.Resource
import com.vangelnum.newsapp.core.data.model.News
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    suspend fun getSearchNews(
        query: String,
        sortBy: String,
        from: String?,
        to: String?,
    ): Flow<Resource<News>>
}