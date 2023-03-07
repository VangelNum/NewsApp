package com.vangelnum.newsapp.feature_search.data

import com.vangelnum.newsapp.core.data.dto.News
import com.vangelnum.newsapp.feature_main.data.api.ApiNews
import com.vangelnum.newsapp.feature_search.domain.repository.SearchRepository
import retrofit2.Response
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val api: ApiNews
): SearchRepository {
    override suspend fun getSearchNews(
        query: String,
        sortBy: String,
        from: String?,
        to: String?
    ): Response<News> {
        return api.getSearchNews(query, sortBy, from, to)
    }
}