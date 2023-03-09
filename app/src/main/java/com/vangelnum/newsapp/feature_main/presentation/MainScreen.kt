package com.vangelnum.newsapp.feature_main.presentation

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import com.vangelnum.newsapp.R
import com.vangelnum.newsapp.feature_favourite.data.model.FavouriteData
import com.vangelnum.newsapp.feature_favourite.presentation.FavouriteViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel(),
    viewModelRoom: FavouriteViewModel = hiltViewModel()
) {
    val photos = viewModel.items.collectAsState().value
    val news = viewModelRoom.readAllData.observeAsState().value
    val context = LocalContext.current
    val listState = rememberLazyListState()
    LazyColumn(state = listState) {
        if (photos.data?.articles!=null) {
            items(photos.data.articles) {
                Box(modifier = Modifier.clickable {
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(it.url))
                    context.startActivity(browserIntent)
                }) {
                    Column {
                        if (it.urlToImage != "null") {
                            Card(
                                shape = RoundedCornerShape(15.dp),
                                modifier = Modifier
                                    .height(250.dp)
                                    .padding(all = 10.dp)
                                    .fillMaxWidth()
                            ) {
                                SubcomposeAsyncImage(
                                    model = it.urlToImage,
                                    contentDescription = "image",
                                    contentScale = ContentScale.FillBounds,
                                    loading = {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .background(Color.Black),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            CircularProgressIndicator(color = Color.Green)
                                        }
                                    }
                                )

                            }
                        }
                        Text(
                            text = it.title,
                            textAlign = TextAlign.Start,
                            modifier = Modifier.padding(start = 15.dp, end = 15.dp),
                            color = Color.White,
                            maxLines = 3,
                            fontSize = 20.sp,
                            overflow = TextOverflow.Ellipsis
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(15.dp),
                        ) {
                            val result = it.publishedAt
                            val result2 = result.substring(0, 10) + ' ' + result.substring(11, 16)

                            Text(text = result2, fontSize = 16.sp)
                            Spacer(modifier = Modifier.weight(1f))

                            var tint by remember {
                                mutableStateOf(Color.White)
                            }

                            news?.forEach { new ->
                                if (it.urlToImage != null) {
                                    if (it.urlToImage.contains(new.urlPhoto)) {
                                        tint = Color.Red
                                    }
                                } else {
                                    tint = Color.White
                                }
                            }
                            CompositionLocalProvider(
                                LocalMinimumInteractiveComponentEnforcement provides false,
                            ) {
                                IconButton(onClick = {
                                    if (tint == Color.White) {
                                        viewModelRoom.addNewsDataBase(
                                            FavouriteData(
                                                it.urlToImage,
                                                it.description,
                                                result2
                                            )
                                        )
                                        tint = Color.Red
                                    } else {
                                        viewModelRoom.deleteNewsDataBase(
                                            FavouriteData(
                                                it.urlToImage,
                                                it.description,
                                                result2
                                            )
                                        )
                                        tint = Color.White
                                    }
                                }) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_baseline_favorite_24),
                                        contentDescription = "favourite", tint = tint
                                    )

                                }
                            }
                            Spacer(modifier = Modifier.width(20.dp))
                            CompositionLocalProvider(
                                LocalMinimumInteractiveComponentEnforcement provides false,
                            ) {
                                IconButton(onClick = {
                                    val sendIntent: Intent = Intent().apply {
                                        action = Intent.ACTION_SEND
                                        putExtra(Intent.EXTRA_TEXT, it.url)
                                        type = "text/plain"
                                    }
                                    val shareIntent = Intent.createChooser(sendIntent, null)
                                    context.startActivity(shareIntent)
                                }) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_baseline_share_24),
                                        contentDescription = "share"
                                    )
                                }
                            }
                        }
                        Divider(
                            modifier = Modifier
                                .height(1.dp)
                                .fillMaxWidth(), color = Color.Gray
                        )
                    }

                }


            }
        }
    }
    val scope = rememberCoroutineScope()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 10.dp, end = 10.dp), contentAlignment = Alignment.BottomEnd
    ) {
        val isAtTop by remember {
            derivedStateOf {
                listState.firstVisibleItemIndex == 0
            }
        }
        AnimatedVisibility(visible = !isAtTop) {
            FloatingActionButton(onClick = {

                if (!isAtTop) {
                    scope.launch { listState.animateScrollToItem(0) }
                }
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_keyboard_arrow_up_24),
                    contentDescription = "arrow"
                )
            }
        }
    }
    if (photos.data?.totalResults == 0) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = Color.Green)
        }
    }
}
