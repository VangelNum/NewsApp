package com.vangelnum.newsapp

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import com.vangelnum.newsapp.data.News


@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
@Composable
fun SearchScreen(viewModel: MainViewModel, news: List<RoomEntity>) {
    val newsFromSearch by viewModel.itemsSearch.collectAsState()
    val context = LocalContext.current
    Column {
        SearchTab(viewModel = viewModel)
        LazyColumn(modifier = Modifier.background(Color.Black)) {

            items(newsFromSearch.articles) {
                Box(modifier = Modifier.clickable {
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(it.url))
                    context.startActivity(browserIntent)
                }) {
                    Column {
                        Card(shape = RoundedCornerShape(15.dp),
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
                                val state by remember {
                                    mutableStateOf(false)
                                }
                                IconButton(onClick = {
                                    viewModel.addNewsDataBase(RoomEntity(it.urlToImage,
                                        it.description,
                                        result2))
                                }) {
                                    news.forEach { new ->
                                        if (it.urlToImage != null) {
                                            if (new.urlPhoto.contains(it.urlToImage)) {
                                                Icon(painter = painterResource(
                                                    id = R.drawable.ic_round_favorite_border_24),
                                                    contentDescription = "favourite",
                                                    tint = Color.Red)
                                            } else {
                                                Icon(painter = painterResource(
                                                    id = R.drawable.ic_baseline_favorite_24),
                                                    contentDescription = "favourite",
                                                    tint = Color.White
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.width(20.dp))
                            CompositionLocalProvider(
                                LocalMinimumTouchTargetEnforcement provides false,
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
                                    androidx.compose.material.Icon(painter = painterResource(id = R.drawable.ic_baseline_share_24),
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
    }

    if (newsFromSearch.totalResults == 0) {
        Log.d("check", newsFromSearch.totalResults.toString())
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "Nothing found")
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun  SearchTab(
    viewModel: MainViewModel
) {
    var value by remember {
        mutableStateOf("")
    }
    var query by remember {
        mutableStateOf("")
    }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(key1 = Unit) {
        focusRequester.requestFocus()
        keyboardController?.show()
    }

    OutlinedTextField(
        value = value,
        onValueChange = {
            value = it
        },

        modifier = Modifier
            .focusRequester(focusRequester = focusRequester)
            .fillMaxWidth()
            .padding(5.dp),
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_search_24),
                contentDescription = "search",
                tint = Color.White
            )
        },
        trailingIcon = {
            IconButton(onClick = { value = "" }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_outline_close_24),
                    contentDescription = "delete",
                    tint = Color.White
                )
            }
        },
        singleLine = true,
        colors = TextFieldDefaults.textFieldColors(
            textColor = Color.White,
            cursorColor = Color.Black,
            leadingIconColor = Color.White,
            trailingIconColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Black,
        ),
        shape = RoundedCornerShape(15.dp),
        placeholder = {
            Text(text = "Search")
        },
        keyboardActions = KeyboardActions(
            onDone = {
                if (value != "") {
                    query = value
                    viewModel.getSearchNews(query)
                    keyboardController?.hide()
                }
            }
        )
    )

}