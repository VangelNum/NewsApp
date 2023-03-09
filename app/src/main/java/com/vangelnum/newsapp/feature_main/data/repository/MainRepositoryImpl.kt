package com.vangelnum.newsapp.feature_main.data.repository

import android.util.Log
import com.vangelnum.newsapp.core.common.Resource
import com.vangelnum.newsapp.core.data.mapper.toDomainModel
import com.vangelnum.newsapp.core.domain.model.News
import com.vangelnum.newsapp.feature_main.data.api.ApiNews
import com.vangelnum.newsapp.feature_main.domain.repository.MainRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MainRepositoryImpl(
    private val api: ApiNews,
) : MainRepository {
    override fun getNews(): Flow<Resource<News>> = flow {
        try {
            val response = api.getNews().toDomainModel()
            Log.d("tag", "Response $response")
            emit(Resource.Success(response))
        } catch (e: Exception) {
            Log.d("tag", "error: ${e.message.toString()}")
            emit(Resource.Error(e.message.toString()))
        }
    }
}