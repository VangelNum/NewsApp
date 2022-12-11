package com.vangelnum.newsapp

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import com.vangelnum.newsapp.data.News
import kotlinx.coroutines.currentCoroutineContext
import kotlin.coroutines.coroutineContext


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainScreen(photos: News) {
    val context = LocalContext.current
    LazyColumn(modifier = Modifier.background(Color.Black)) {
        items(photos.articles) {
            Box(modifier = Modifier.clickable {
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, it.url)
                    type = "text/plain"
                }
                val shareIntent = Intent.createChooser(sendIntent, null)
                context.startActivity(shareIntent)
            }) {
                Column() {
                    Card(shape = RoundedCornerShape(15.dp),
                        modifier = Modifier
                            .height(250.dp)
                            .padding(all = 10.dp)
                    ) {
                        SubcomposeAsyncImage(
                            model = it.urlToImage,
                            contentDescription = "image",
                            contentScale = ContentScale.Crop,
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
                        CompositionLocalProvider(
                            LocalMinimumTouchTargetEnforcement provides false,
                        ) {
                            IconButton(onClick = { }) {
                                Icon(painter = painterResource(id = R.drawable.ic_round_favorite_border_24),
                                    contentDescription = "favourite")
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        CompositionLocalProvider(
                            LocalMinimumTouchTargetEnforcement provides false,
                        ) {
                            IconButton(onClick = { }) {
                                Icon(painter = painterResource(id = R.drawable.ic_baseline_share_24),
                                    contentDescription = "share")
                            }
                        }
                    }
                    Divider(modifier = Modifier
                        .height(1.dp)
                        .fillMaxWidth(), color = Color.Gray)
                }

            }


        }
    }
    if (photos.totalResults == 0){
        Log.d("check",photos.totalResults.toString())
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = Color.Green)
        }
    }
}