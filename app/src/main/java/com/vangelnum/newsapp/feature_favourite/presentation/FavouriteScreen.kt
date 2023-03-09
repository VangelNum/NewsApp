package com.vangelnum.newsapp.feature_favourite.presentation

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FavouriteScreen(viewModel: FavouriteViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val news = viewModel.readAllData.observeAsState()

    LazyColumn(modifier = Modifier.background(Color.Black)) {
        items(news.value ?: emptyList()) {
            Box(modifier = Modifier.clickable {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(it.urlPhoto))
                context.startActivity(browserIntent)
            }) {
                Column {
                    Card(
                        shape = RoundedCornerShape(15.dp),
                        modifier = Modifier
                            .height(250.dp)
                            .padding(all = 10.dp)
                            .fillMaxWidth()
                    ) {
                        SubcomposeAsyncImage(
                            model = it.urlPhoto,
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
                    Text(
                        text = it.content,
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

                        Text(text = it.time, fontSize = 16.sp)
                        Spacer(modifier = Modifier.weight(1f))
                        CompositionLocalProvider(
                            LocalMinimumInteractiveComponentEnforcement provides false,
                        ) {
                            IconButton(onClick = {
                                viewModel.deleteNewsDataBase(
                                    FavouriteData(
                                        it.urlPhoto,
                                        it.content,
                                        it.time
                                    )
                                )
                            }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_baseline_delete_24),
                                    contentDescription = "favourite", tint = Color.White
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
                                    putExtra(Intent.EXTRA_TEXT, it.urlPhoto)
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