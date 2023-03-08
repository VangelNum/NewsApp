package com.vangelnum.newsapp.feature_search.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vangelnum.newsapp.core.data.dto.NewsDto
import com.vangelnum.newsapp.feature_search.domain.repository.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: SearchRepository
): ViewModel() {

    private val _itemsSearch = MutableStateFlow(NewsDto(emptyList(), "", 0))
    var itemsSearch: StateFlow<NewsDto> = _itemsSearch

    fun getSearchNews(query: String, sortBy: String, from: String?, to: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getSearchNews(query, sortBy, from, to)
            if (response.isSuccessful) {
                _itemsSearch.value = response.body()!!
            }
        }
    }
    private val _stateFlow = MutableStateFlow("")
    val stateFlow = _stateFlow.asStateFlow()

    fun triggerStateFlow(query: String) {
        _stateFlow.value = query
    }

}