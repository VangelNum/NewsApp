package com.vangelnum.newsapp.feature_main.presentation

import android.content.Context
import android.content.Context.VIBRATOR_MANAGER_SERVICE
import android.content.Context.VIBRATOR_SERVICE
import android.content.Intent
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
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
                favouriteState = favouriteState
            )
        }

    }
}

@Composable
fun MainContent(
    items: News,
    favouriteViewModel: FavouriteViewModel,
    favouriteState: State<Resource<List<FavouriteData>>>
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
                MainCard(article = article, favouriteViewModel = favouriteViewModel, favouriteState)
                if (index < items.articles.lastIndex) {
                    Divider(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.onSurface)
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
    favouriteState: State<Resource<List<FavouriteData>>>
) {
    Card(
        shape = MaterialTheme.shapes.large
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
    MainTitle(article = article, favouriteViewModel = favouriteViewModel, favouriteState)
}

@Composable
fun MainTitle(
    article: Article,
    favouriteViewModel: FavouriteViewModel,
    favouriteState: State<Resource<List<FavouriteData>>>
) {
    Text(
        text = article.title,
        maxLines = 3,
        overflow = TextOverflow.Ellipsis,
        style = MaterialTheme.typography.h3,
    )
    MainRowItems(article, favouriteViewModel, itemsFavourite = favouriteState)
}


@Composable
fun MainRowItems(
    article: Article,
    favouriteViewModel: FavouriteViewModel,
    itemsFavourite: State<Resource<List<FavouriteData>>>
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
            vibrate(context)
            if (itemsFavourite.value.data?.toString()?.contains(article.urlToImage) == false) {
                addToDatabase(favouriteViewModel, article.urlToImage, article.content, publishedAt)
            } else {
                deleteFromDatabase(
                    favouriteViewModel,
                    article.urlToImage,
                    article.content,
                    publishedAt
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
            share(context, article.url)
        }) {
            Icon(imageVector = Icons.Outlined.Share, contentDescription = "share")
        }
    }
}

private fun addToDatabase(
    favouriteViewModel: FavouriteViewModel,
    urlToImage: String,
    content: String,
    publishedAt: String
) {
    favouriteViewModel.addNewsDataBase(
        FavouriteData(
            urlToImage,
            content,
            publishedAt
        )
    )
}

private fun deleteFromDatabase(
    favouriteViewModel: FavouriteViewModel,
    urlToImage: String,
    content: String,
    publishedAt: String
) {
    favouriteViewModel.deleteNewsDataBase(
        FavouriteData(
            urlToImage,
            content,
            publishedAt
        )
    )
}

private fun share(context: Context, url: String) {
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, url)
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, null)
    context.startActivity(shareIntent)
}

@Suppress("DEPRECATION")
private fun vibrate(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager = context.getSystemService(VIBRATOR_MANAGER_SERVICE) as VibratorManager
        val vibrator = vibratorManager.defaultVibrator
        vibrator.vibrate(VibrationEffect.createOneShot(70, VibrationEffect.DEFAULT_AMPLITUDE))
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val vibrator = context.getSystemService(VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(VibrationEffect.createOneShot(70, VibrationEffect.DEFAULT_AMPLITUDE))
    } else {
        val vibrator = context.getSystemService(VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(70)
    }
}