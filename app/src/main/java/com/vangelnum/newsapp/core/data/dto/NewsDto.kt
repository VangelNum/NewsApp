package com.vangelnum.newsapp.core.data.dto

data class NewsDto(
    val articles: List<ArticleDto>,
    val status: String?,
    var totalResults: Int
)