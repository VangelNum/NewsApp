package com.vangelnum.newsapp.feature_favourite.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vangelnum.newsapp.core.common.Resource
import com.vangelnum.newsapp.feature_favourite.data.model.FavouriteData
import com.vangelnum.newsapp.feature_favourite.domain.repository.FavouriteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouriteViewModel @Inject constructor(
    private val repository: FavouriteRepository
) : ViewModel() {
//    var readAllData: LiveData<List<FavouriteData>> = repository.getAll()

    private val _readAllData = MutableStateFlow<Resource<List<FavouriteData>>>(Resource.Loading())
    val readAllData = _readAllData.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getAll().collect { res->
                _readAllData.value = res
            }
        }
    }

    fun addNewsDataBase(news: FavouriteData) {
        viewModelScope.launch {
            repository.addNews(news)
        }
    }

    fun deleteNewsDataBase(news: FavouriteData) {
        viewModelScope.launch {
            repository.deleteNews(news)
        }
    }
}