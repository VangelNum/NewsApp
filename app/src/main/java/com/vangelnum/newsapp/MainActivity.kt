package com.vangelnum.newsapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: MainViewModel by viewModels()
            val photos by viewModel.items.collectAsState()
            LazyColumn(modifier = Modifier.background(Color.Black)) {
                items(photos.articles) {
                    if (it.urlToImage != null) {
                        Log.d("Tag",it.urlToImage + "")
                        Card(shape = RoundedCornerShape(15.dp),
                            modifier = Modifier.height(250.dp).padding(all = 10.dp)
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
                    }
                    SelectionContainer {
                        Text(
                            text = it.title,
                            textAlign = TextAlign.Start,
                            modifier = Modifier.padding(start = 10.dp, end = 10.dp, bottom = 10.dp),
                            color = Color.White,
                            maxLines = 3,
                            fontSize = 20.sp,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Spacer(modifier = Modifier.height(5.dp))
                    Divider(modifier = Modifier
                        .height(1.dp)
                        .fillMaxWidth(), color = Color.Black)
                }
            }
        }
    }
}
