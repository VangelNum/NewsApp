package com.vangelnum.newsapp.core.data.dto

data class ArticleDto(
    val author: String?,
    val content: String,
    val description: String?,
    val publishedAt: String,
    val source: SourceDto,
    val title: String,
    val url: String,
    val urlToImage: String?
)