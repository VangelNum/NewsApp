package com.vangelnum.newsapp.feature_search.data.repository

import com.vangelnum.newsapp.core.common.Resource
import com.vangelnum.newsapp.core.data.mapper.toDomain
import com.vangelnum.newsapp.core.data.model.News
import com.vangelnum.newsapp.feature_search.data.api.ApiSearch
import com.vangelnum.newsapp.feature_search.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val api: ApiSearch
) : SearchRepository {
    override suspend fun getSearchNews(
        query: String,
        sortBy: String,
        from: String?,
        to: String?
    ): Flow<Resource<News>> = flow {
        try {
            val response = api.getSearchNews(query, sortBy, from, to)
            emit(Resource.Success(response.toDomain()))
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString()))
        }
    }
}