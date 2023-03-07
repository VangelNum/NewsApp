package com.vangelnum.newsapp.core.data.dto

data class News(
    val articles: List<Article>,
    val status: String?,
    var totalResults: Int
)