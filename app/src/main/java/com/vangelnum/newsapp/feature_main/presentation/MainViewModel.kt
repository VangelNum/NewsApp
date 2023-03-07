package com.vangelnum.newsapp.feature_main.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vangelnum.newsapp.core.data.dto.News
import com.vangelnum.newsapp.feature_main.domain.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MainRepository,
) : ViewModel() {

    private val _items = MutableStateFlow(News(emptyList(), "", 0))
    var items: StateFlow<News> = _items

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getNews()
            if (response.isSuccessful) {
                _items.value = response.body()!!
            }
        }
    }

}