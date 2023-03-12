package com.vangelnum.newsapp.feature_favourite.presentation

import android.content.Context
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
import androidx.compose.foundation.lazy.items
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
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material.icons.outlined.Share
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
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
import coil.compose.SubcomposeAsyncImage
import com.vangelnum.newsapp.R
import com.vangelnum.newsapp.core.common.Resource
import com.vangelnum.newsapp.feature_favourite.data.model.FavouriteData
import kotlinx.coroutines.launch

@Composable
fun FavouriteScreen(
    favouriteViewModel: FavouriteViewModel,
    favouriteState: State<Resource<List<FavouriteData>>>
) {

    when (favouriteState.value) {
        is Resource.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = favouriteState.value.message.toString())
            }
        }

        is Resource.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is Resource.Success -> {
            if (favouriteState.value.data?.isNotEmpty() == true) {
                val lazyListState = rememberLazyListState()
                val isVisible = remember { derivedStateOf { lazyListState.firstVisibleItemIndex > 0 } }
                LazyColumn(
                    state = lazyListState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(all = 16.dp)
                ) {

                    items(favouriteState.value.data!!) { favouriteData ->
                        FavouriteCard(favouriteData, favouriteViewModel)
                    }
                }
                val scope = rememberCoroutineScope()
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
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = stringResource(id = R.string.empty_favourite))
                }
            }
        }
    }
}

@Composable
fun FavouriteCard(favouriteData: FavouriteData, favouriteViewModel: FavouriteViewModel) {
    Card(
        shape = MaterialTheme.shapes.large
    ) {
        SubcomposeAsyncImage(
            model = favouriteData.urlPhoto,
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
    Spacer(modifier = Modifier.height(8.dp))
    FavouriteTitle(favouriteData, favouriteViewModel = favouriteViewModel)
}

@Composable
fun FavouriteTitle(favouriteData: FavouriteData, favouriteViewModel: FavouriteViewModel) {
    Text(
        text = favouriteData.content,
        maxLines = 3,
        overflow = TextOverflow.Ellipsis,
        style = MaterialTheme.typography.h3,
    )
    FavouriteRowItems(favouriteData, favouriteViewModel = favouriteViewModel)
}

@Composable
fun FavouriteRowItems(favouriteData: FavouriteData, favouriteViewModel: FavouriteViewModel) {
    val context = LocalContext.current
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = favouriteData.time)
        Spacer(modifier = Modifier.weight(1f))
        IconButton(onClick = {
            vibrate(context)
            favouriteViewModel.deleteNewsDataBase(
                FavouriteData(
                    favouriteData.urlPhoto,
                    favouriteData.content,
                    favouriteData.time
                )
            )
        }) {
            Icon(
                imageVector = Icons.Outlined.Delete,
                contentDescription = "delete",
            )
        }
        IconButton(onClick = {
            share(context, favouriteData.urlPhoto)
        }) {
            Icon(imageVector = Icons.Outlined.Share, contentDescription = "share")
        }
    }
    Divider(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.onSurface)
    Spacer(modifier = Modifier.height(16.dp))
}

private fun share(context: Context, urlPhoto: String) {
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, urlPhoto)
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

