package com.vangelnum.newsapp.data

data class News(
    val articles: List<Article>,
    val status: String?,
    var totalResults: Int
)