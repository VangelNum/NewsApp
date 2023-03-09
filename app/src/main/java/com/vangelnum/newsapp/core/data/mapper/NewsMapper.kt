package com.vangelnum.newsapp.core.data.mapper

import com.vangelnum.newsapp.core.data.dto.ArticleDto
import com.vangelnum.newsapp.core.data.dto.NewsDto
import com.vangelnum.newsapp.core.data.dto.SourceDto
import com.vangelnum.newsapp.core.domain.model.Article
import com.vangelnum.newsapp.core.domain.model.News
import com.vangelnum.newsapp.core.domain.model.Source


fun NewsDto.toDomainModel(): News {
    return News(
        articles = articles.map { it.toDomainModel() },
        status = status,
        totalResults = totalResults
    )
}

fun ArticleDto.toDomainModel(): Article {
    return Article(
        author = author?: "",
        content = content?: "",
        description = description?: "",
        publishedAt = publishedAt?: "",
        source = source.toDomainModel(),
        title = title?: "",
        url = url?: "",
        urlToImage = urlToImage?: ""
    )
}

fun SourceDto.toDomainModel(): Source {
    return Source(
        id = id ?: "",
        name = name?: ""
    )
}


//fun NewsDto.toNews(): News {
//    return News(
//        articles = this.articles.map { it.toArticle() },
//        status = this.status ?: "",
//        totalResults = this.totalResults
//    )
//}
//
//fun ArticleDto.toArticle(): Article {
//    return Article(
//        author = this.author ?: "",
//        content = this.content ?: "",
//        description = this.description ?: "",
//        publishedAt = this.publishedAt ?: "",
//        source = this.source.toSource(),
//        title = this.title,
//        url = this.url,
//        urlToImage = this.urlToImage ?: ""
//    )
//}
//
//fun SourceDto.toSource(): Source {
//    return Source(
//        id = this.id ?: "",
//        name = this.name
//    )
//}
