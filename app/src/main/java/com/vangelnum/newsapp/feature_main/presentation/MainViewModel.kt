package com.vangelnum.newsapp.feature_main.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vangelnum.newsapp.core.common.Resource
import com.vangelnum.newsapp.core.domain.model.News
import com.vangelnum.newsapp.feature_main.domain.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MainRepository,
) : ViewModel() {

    private val _items = MutableStateFlow<Resource<News>>(Resource.Loading())
    var items = _items.asStateFlow()


    init {
        getNews()
    }

    fun getNews() {
        viewModelScope.launch {
            repository.getNews().collect {
                _items.value = it
            }
        }
    }

}