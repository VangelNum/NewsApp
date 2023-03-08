package com.vangelnum.newsapp.core.data.mapper

import com.vangelnum.newsapp.core.data.dto.ArticleDto
import com.vangelnum.newsapp.core.data.dto.NewsDto
import com.vangelnum.newsapp.core.data.dto.SourceDto
import com.vangelnum.newsapp.core.data.model.Article
import com.vangelnum.newsapp.core.data.model.News
import com.vangelnum.newsapp.core.data.model.Source


fun NewsDto.toDomain(): News {
    return News(
        status = status,
        totalResults = totalResults,
        articles = articles.toDomain()
    )
}

fun List<ArticleDto>.toDomain(): List<Article> {
    return this.map {
        Article(
            author = it.author,
            content = it.content,
            description = it.description,
            publishedAt = it.publishedAt,
            title = it.title,
            url = it.url,
            urlToImage = it.urlToImage,
            source = it.source.toDomain()
        )
    }
}

private fun SourceDto.toDomain(): Source {
    return Source(
        id = id,
        name = name
    )
}
