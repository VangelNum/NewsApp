package com.vangelnum.newsapp.feature_main.domain.repository

import com.vangelnum.newsapp.core.common.Resource
import com.vangelnum.newsapp.core.domain.model.News
import kotlinx.coroutines.flow.Flow

interface MainRepository {
    fun getNews(): Flow<Resource<News>>
}