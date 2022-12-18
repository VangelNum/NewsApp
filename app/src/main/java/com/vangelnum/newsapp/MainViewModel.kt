package com.vangelnum.newsapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vangelnum.newsapp.data.News
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MyRepository,
    private val repositoryRoom: RoomRepository
) : ViewModel() {


     fun addNewsDataBase(news: RoomEntity) {
        viewModelScope.launch {
            repositoryRoom.addNews(news)
        }
    }
    fun deleteNewsDataBase(news: RoomEntity) {
        viewModelScope.launch {
            repositoryRoom.deleteNews(news)
        }
    }

    fun getSearchNews(query: String) {
        viewModelScope.launch (Dispatchers.IO) {
            val response = repository.getSearchNews(query)
            if (response.isSuccessful) {
                _itemsSearch.value = response.body()!!
            }
        }
    }

    var readAllData: LiveData<List<RoomEntity>> = repositoryRoom.getAll()

    private val _items = MutableStateFlow(News(emptyList(), "", 0))
    var items: StateFlow<News> = _items

    private val _itemsSearch = MutableStateFlow(News(emptyList(), "", 0))
    var itemsSearch: StateFlow<News> = _itemsSearch

    init {
        viewModelScope.launch {
            val response = repository.getNews()
            if (response.isSuccessful) {
                _items.value = response.body()!!
            }
        }
    }

}