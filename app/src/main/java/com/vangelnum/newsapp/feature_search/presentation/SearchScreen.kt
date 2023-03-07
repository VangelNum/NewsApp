package com.vangelnum.newsapp.feature_search.presentation

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import com.vangelnum.newsapp.R
import com.vangelnum.newsapp.core.presentation.Screens
import com.vangelnum.newsapp.feature_favourite.data.model.FavouriteData
import com.vangelnum.newsapp.feature_favourite.presentation.FavouriteViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SearchScreen(
    viewModelSearch: SearchViewModel = hiltViewModel(),
    viewModel: FavouriteViewModel = hiltViewModel(),
    scaffoldState: ScaffoldState,
    navController: NavController,
) {
    val newsFromSearch = viewModelSearch.itemsSearch.collectAsState().value
    val news = viewModel.readAllData.observeAsState().value
    val context = LocalContext.current
    Column {
        SearchTab(
            viewModel = viewModelSearch,
            scaffoldState = scaffoldState,
            navController = navController
        )
        LazyColumn(modifier = Modifier.background(Color.Black)) {

            items(newsFromSearch.articles) {
                Box(modifier = Modifier.clickable {
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(it.url))
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
                                    tint = if (tint == Color.White) {
                                        viewModel.addNewsDataBase(
                                            FavouriteData(
                                                it.urlToImage,
                                                it.description,
                                                result2
                                            )
                                        )
                                        Color.Red
                                    } else {
                                        viewModel.deleteNewsDataBase(
                                            FavouriteData(
                                                it.urlToImage,
                                                it.description,
                                                result2
                                            )
                                        )
                                        Color.White
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
                                        contentDescription = "share",
                                        tint = Color.White
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

    if (newsFromSearch.totalResults == 0) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "Nothing found")
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
@Composable
fun SearchTab(
    viewModel: SearchViewModel,
    scaffoldState: ScaffoldState,
    navController: NavController,
) {


    val query = viewModel.stateFlow.collectAsState()

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }

    val listItems = listOf("publishedAt", "popularity", "relevancy")

    var selectedItem by remember {
        mutableStateOf(listItems[0])
    }

    LaunchedEffect(key1 = Unit) {
        focusRequester.requestFocus()
        keyboardController?.show()
    }
    OutlinedTextField(
        value = query.value,
        onValueChange = {
            viewModel.triggerStateFlow(it)
        },
        modifier = Modifier
            .focusRequester(focusRequester = focusRequester)
            .fillMaxWidth()
            .padding(all = 10.dp),
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_search_24),
                contentDescription = "search",
                tint = Color.White
            )
        },
        trailingIcon = {
            IconButton(onClick = {
                viewModel.triggerStateFlow("")
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_outline_close_24),
                    contentDescription = "close",
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
            Text(text = "Search Something")
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(
            onSearch = {
                if (query.value != "") {
                    viewModel.getSearchNews(query.value, selectedItem, null, null)
                    keyboardController?.hide()
                }
            }
        )
    )

    if (query.value != "") {
        var expanded by remember {
            mutableStateOf(false)
        }

        val scope = rememberCoroutineScope()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {
                    expanded = !expanded
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
            ) {
                OutlinedTextField(
                    value = selectedItem,
                    onValueChange = {

                    },
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = expanded
                        )
                    },
                    modifier = Modifier.height(60.dp),
                    singleLine = true,
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = MaterialTheme.colors.onSurface.copy(alpha = 0.12f)
                            .compositeOver(MaterialTheme.colors.surface),
                        backgroundColor = Color.Transparent,
                        unfocusedIndicatorColor = MaterialTheme.colors.onSurface.copy(alpha = 0.12f)
                            .compositeOver(MaterialTheme.colors.surface)
                    )
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    listItems.forEach { currentItem ->
                        DropdownMenuItem(onClick = {
                            selectedItem = currentItem
                            expanded = false
                            if (query.value != "") {
                                viewModel.getSearchNews(
                                    query = query.value,
                                    sortBy = currentItem,
                                    null,
                                    null
                                )
                            } else {
                                scope.launch {
                                    scaffoldState.snackbarHostState.showSnackbar(
                                        message = "Need some query"
                                    )
                                }
                            }
                        }) {
                            Text(text = currentItem)
                        }
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                OutlinedButton(
                    onClick = {
                        navController.navigate(Screens.FilterScreen.route + "/${query.value}/$selectedItem")
                    }, modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_filter_list_24),
                        contentDescription = "sort",
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = "Filters", color = Color.White)
                }
            }
        }
    }
}
