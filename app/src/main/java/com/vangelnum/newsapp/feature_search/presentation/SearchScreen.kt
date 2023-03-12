package com.vangelnum.newsapp.feature_search.presentation

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Share
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
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
import com.vangelnum.newsapp.feature_search.data.common.SearchResource


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchScreen(
    viewModelSearch: SearchViewModel = hiltViewModel(),
    favouriteState: State<Resource<List<FavouriteData>>>,
    favouriteViewModel: FavouriteViewModel
) {
    val newsFromSearch = viewModelSearch.itemsSearch.collectAsState().value

    var textValue by rememberSaveable {
        mutableStateOf("")
    }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val keyBoard = LocalSoftwareKeyboardController.current
    LaunchedEffect(key1 = true) {
        focusRequester.requestFocus()
    }
    Column(modifier = Modifier.fillMaxSize()) {
        OutlinedTextField(
            value = textValue,
            onValueChange = { newText ->
                textValue = newText
            },
            trailingIcon = {
                IconButton(onClick = { textValue = "" }) {
                    Icon(
                        imageVector = Icons.Outlined.Close,
                        contentDescription = "close"
                    )
                }
            },
            leadingIcon = {
                IconButton(onClick = {
                    focusManager.clearFocus()
                    keyBoard?.hide()
                    viewModelSearch.getSearchNews(
                        textValue,
                        "publishedAt",
                        null,
                        null
                    )
                }) {
                    Icon(
                        imageVector = Icons.Outlined.Search,
                        contentDescription = "search"
                    )
                }
            },
            label = { Text(text = stringResource(id = R.string.search)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .focusRequester(focusRequester),
            maxLines = 1,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = {
                    focusManager.clearFocus()
                    keyBoard?.hide()
                    viewModelSearch.getSearchNews(textValue, "publishedAt", null, null)
                }
            )
        )
        when (newsFromSearch) {
            is SearchResource.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            is SearchResource.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = newsFromSearch.message.toString())
                }
            }

            is SearchResource.Success -> {
                FavouriteContent(newsFromSearch, favouriteState, favouriteViewModel)
            }

            is SearchResource.Empty -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = stringResource(id = R.string.search_something))
                }
            }
        }
    }


}

@Composable
fun FavouriteContent(
    newsFromSearch: SearchResource<News>,
    favouriteState: State<Resource<List<FavouriteData>>>,
    favouriteViewModel: FavouriteViewModel
) {
    val lazyListState = rememberLazyListState()

    if (newsFromSearch.data?.articles?.isNotEmpty() == true) {
        LazyColumn(
            state = lazyListState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp)
        ) {
            itemsIndexed(newsFromSearch.data.articles) { index, item ->
                if (item.urlToImage.isNotEmpty()) {
                    SearchCard(item, favouriteState, favouriteViewModel)
                    if (index < newsFromSearch.data.articles.lastIndex) {
                        Divider(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colors.onSurface
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    } else {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = stringResource(id = R.string.nothing_found))
        }
    }
}

@Composable
fun SearchCard(
    item: Article,
    favouriteState: State<Resource<List<FavouriteData>>>,
    favouriteViewModel: FavouriteViewModel
) {
    Card(
        shape = MaterialTheme.shapes.large
    ) {
        SubcomposeAsyncImage(
            model = item.urlToImage,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentScale = ContentScale.Crop,
            loading = {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            },
            error = {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = stringResource(id = R.string.error_image))
                }
            }
        )

    }
    Spacer(modifier = Modifier.height(8.dp))
    SearchTitle(item, favouriteState, favouriteViewModel)
}

@Composable
fun SearchTitle(
    article: Article,
    favouriteState: State<Resource<List<FavouriteData>>>,
    favouriteViewModel: FavouriteViewModel
) {
    Text(
        text = article.title,
        maxLines = 3,
        overflow = TextOverflow.Ellipsis,
        style = MaterialTheme.typography.h3,
    )
    SearchRowItems(article, favouriteState, favouriteViewModel)


}

@Composable
fun SearchRowItems(
    article: Article,
    favouriteState: State<Resource<List<FavouriteData>>>,
    favouriteViewModel: FavouriteViewModel
) {
    val context = LocalContext.current
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val publishedTime =
            article.publishedAt.substring(0, 10) + ' ' + article.publishedAt.substring(11, 16)
        Text(text = publishedTime)
        Spacer(modifier = Modifier.weight(1f))

        IconButton(onClick = {
            vibrate(context)
            if (favouriteState.value.data?.toString()?.contains(article.urlToImage) == false) {
                addToDatabase(
                    favouriteViewModel,
                    article.urlToImage,
                    article.content,
                    article.publishedAt
                )
            } else {
                deleteFromDatabase(
                    favouriteViewModel,
                    article.urlToImage,
                    article.content,
                    article.publishedAt
                )
            }
        }) {
            Icon(
                imageVector = Icons.Outlined.Favorite,
                contentDescription = "favourite",
                tint = if (favouriteState.value.data?.toString()
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
        val vibratorManager =
            context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        val vibrator = vibratorManager.defaultVibrator
        vibrator.vibrate(VibrationEffect.createOneShot(70, VibrationEffect.DEFAULT_AMPLITUDE))
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(VibrationEffect.createOneShot(70, VibrationEffect.DEFAULT_AMPLITUDE))
    } else {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(70)
    }
}