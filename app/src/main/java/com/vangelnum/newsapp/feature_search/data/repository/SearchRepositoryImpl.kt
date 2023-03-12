package com.vangelnum.newsapp.feature_search.data.repository

import com.vangelnum.newsapp.core.data.mapper.toDomainModel
import com.vangelnum.newsapp.core.domain.model.News
import com.vangelnum.newsapp.feature_search.data.api.ApiSearch
import com.vangelnum.newsapp.feature_search.data.common.SearchResource
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
    ): Flow<SearchResource<News>> = flow {
        emit(SearchResource.Loading())
        try {
            val response = api.getSearchNews(query, sortBy, from, to).toDomainModel()
            emit(SearchResource.Success(response))
        } catch (e: Exception) {
            emit(SearchResource.Error(e.message.toString()))
        }
    }
}