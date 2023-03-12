package com.vangelnum.newsapp.feature_main.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material.icons.outlined.Share
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import com.vangelnum.newsapp.R
import com.vangelnum.newsapp.core.common.Resource
import com.vangelnum.newsapp.core.domain.model.Article
import com.vangelnum.newsapp.core.domain.model.News
import com.vangelnum.newsapp.feature_favourite.data.model.FavouriteData
import com.vangelnum.newsapp.feature_favourite.presentation.FavouriteViewModel
import kotlinx.coroutines.launch


@Composable
fun MainScreen(
    favouriteViewModel: FavouriteViewModel,
    viewModelMain: MainViewModel = hiltViewModel(),
    favouriteState: State<Resource<List<FavouriteData>>>
) {
    val response = viewModelMain.items.collectAsState()
    when (response.value) {
        is Resource.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is Resource.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = response.value.message.toString())
            }
        }

        is Resource.Success -> {
            MainContent(
                items = response.value.data!!,
                favouriteViewModel = favouriteViewModel,
                favouriteState = favouriteState,
                viewModelMain = viewModelMain
            )
        }

    }
}

@Composable
fun MainContent(
    items: News,
    favouriteViewModel: FavouriteViewModel,
    favouriteState: State<Resource<List<FavouriteData>>>,
    viewModelMain: MainViewModel
) {
    val lazyListState = rememberLazyListState()
    val isVisible = remember { derivedStateOf { lazyListState.firstVisibleItemIndex > 0 } }
    val scope = rememberCoroutineScope()
    LazyColumn(
        state = lazyListState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 80.dp)
    ) {
        itemsIndexed(items.articles) { index, article ->
            if (article.urlToImage.isNotEmpty()) {
                MainCard(
                    article = article,
                    favouriteViewModel = favouriteViewModel,
                    favouriteState = favouriteState,
                    viewModelMain = viewModelMain
                )
                if (index < items.articles.lastIndex) {
                    Divider(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colors.onSurface
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd) {
        AnimatedVisibility(visible = isVisible.value,
            enter = slideInHorizontally {
                it
            }, exit = slideOutHorizontally {
                it
            }) {
            FloatingActionButton(
                modifier = Modifier
                    .padding(16.dp),
                backgroundColor = MaterialTheme.colors.onSurface,
                contentColor = Color.Black,
                onClick = {
                    scope.launch {
                        lazyListState.animateScrollToItem(0)
                    }
                }) {
                Icon(imageVector = Icons.Outlined.KeyboardArrowUp, contentDescription = "up")
            }
        }
    }
}

@Composable
fun MainCard(
    article: Article,
    favouriteViewModel: FavouriteViewModel,
    favouriteState: State<Resource<List<FavouriteData>>>,
    viewModelMain: MainViewModel
) {
    val context = LocalContext.current
    Card(
        shape = MaterialTheme.shapes.large,
        modifier = Modifier.clickable {
            viewModelMain.goToBrowser(article.url, context)
        }
    ) {
        if (article.urlToImage != "") {
            SubcomposeAsyncImage(
                model = article.urlToImage,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop,
                loading = {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                },
                error = {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = stringResource(id = R.string.error_image))
                    }
                }
            )
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
    MainTitle(
        article = article,
        favouriteViewModel = favouriteViewModel,
        favouriteState = favouriteState,
        viewModelMain = viewModelMain
    )
}

@Composable
fun MainTitle(
    article: Article,
    favouriteViewModel: FavouriteViewModel,
    favouriteState: State<Resource<List<FavouriteData>>>,
    viewModelMain: MainViewModel
) {
    Text(
        text = article.title,
        maxLines = 3,
        overflow = TextOverflow.Ellipsis,
        style = MaterialTheme.typography.h3,
    )
    MainRowItems(
        article = article,
        favouriteViewModel = favouriteViewModel,
        itemsFavourite = favouriteState,
        viewModelMain = viewModelMain
    )
}


@Composable
fun MainRowItems(
    article: Article,
    favouriteViewModel: FavouriteViewModel,
    itemsFavourite: State<Resource<List<FavouriteData>>>,
    viewModelMain: MainViewModel
) {
    val context = LocalContext.current
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val publishedAt =
            article.publishedAt.substring(0, 10) + ' ' + article.publishedAt.substring(11, 16)
        Text(text = publishedAt)
        Spacer(modifier = Modifier.weight(1f))
        IconButton(onClick = {
            viewModelMain.vibrate(context)
            if (itemsFavourite.value.data?.toString()?.contains(article.urlToImage) == false) {
                addToDatabase(
                    favouriteViewModel = favouriteViewModel,
                    url = article.url,
                    urlToImage = article.urlToImage,
                    content = article.content,
                    publishedAt = article.publishedAt
                )
            } else {
                deleteFromDatabase(
                    favouriteViewModel = favouriteViewModel,
                    url = article.url,
                    urlToImage = article.urlToImage,
                    content = article.content,
                    publishedAt = article.publishedAt
                )
            }
        }) {
            Icon(
                imageVector = Icons.Outlined.Favorite,
                contentDescription = "favourite",
                tint = if (itemsFavourite.value.data?.toString()
                        ?.contains(article.urlToImage) == true
                ) Color.Red else Color.White
            )
        }
        IconButton(onClick = {
            viewModelMain.share(context, article.url)
        }) {
            Icon(imageVector = Icons.Outlined.Share, contentDescription = "share")
        }
    }
}

private fun addToDatabase(
    favouriteViewModel: FavouriteViewModel,
    url: String,
    urlToImage: String,
    content: String,
    publishedAt: String
) {
    favouriteViewModel.addNewsDataBase(
        FavouriteData(
            urlPhoto = urlToImage,
            url = url,
            content = content,
            time = publishedAt
        )
    )
}

private fun deleteFromDatabase(
    favouriteViewModel: FavouriteViewModel,
    url: String,
    urlToImage: String,
    content: String,
    publishedAt: String
) {
    favouriteViewModel.deleteNewsDataBase(
        FavouriteData(
            urlPhoto = urlToImage,
            url = url,
            content = content,
            time = publishedAt
        )
    )
}
