package com.vangelnum.newsapp.feature_search.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vangelnum.newsapp.core.common.Resource
import com.vangelnum.newsapp.core.data.model.News
import com.vangelnum.newsapp.feature_search.domain.repository.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: SearchRepository
) : ViewModel() {

    private val _itemsSearch = MutableStateFlow<Resource<News>>(Resource.Loading())
    var itemsSearch = _itemsSearch.asStateFlow()

    fun getSearchNews(query: String, sortBy: String, from: String?, to: String?) {
        viewModelScope.launch {
            repository.getSearchNews(query, sortBy, from, to).collect {
                _itemsSearch.value = it
            }
        }
    }

    private val _stateFlow = MutableStateFlow("")
    val stateFlow = _stateFlow.asStateFlow()

    fun triggerStateFlow(query: String) {
        _stateFlow.value = query
    }

}