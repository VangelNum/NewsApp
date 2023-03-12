package com.vangelnum.newsapp.feature_main.data.repository

import com.vangelnum.newsapp.core.common.Resource
import com.vangelnum.newsapp.core.data.mapper.toDomainModel
import com.vangelnum.newsapp.core.domain.model.News
import com.vangelnum.newsapp.feature_main.data.api.ApiNews
import com.vangelnum.newsapp.feature_main.domain.repository.MainRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val api: ApiNews,
) : MainRepository {
    override fun getNews(): Flow<Resource<News>> = flow {
        try {
            val response = api.getNews().toDomainModel()
            emit(Resource.Success(response))
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString()))
        }
    }
}