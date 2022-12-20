package com.vangelnum.newsapp

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.DatePicker
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Menu
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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import java.util.*


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
                            var tint by remember {
                                mutableStateOf(Color.White)
                            }

                            news.forEach { new ->
                                if (it.urlToImage != null) {
                                    if (it.urlToImage.contains(new.urlPhoto)) {
                                        tint = Color.Red
                                    }
                                } else {
                                    tint = Color.White
                                }
                            }
                            CompositionLocalProvider(
                                LocalMinimumTouchTargetEnforcement provides false,
                            ) {
                                IconButton(onClick = {
                                    if (tint == Color.White) {
                                        viewModel.addNewsDataBase(RoomEntity(it.urlToImage,
                                            it.description,
                                            result2))
                                        tint = Color.Red
                                    } else {
                                        viewModel.deleteNewsDataBase(RoomEntity(it.urlToImage,
                                            it.description,
                                            result2))
                                        tint = Color.White
                                    }
                                }) {
                                    Icon(painter = painterResource(id = R.drawable.ic_baseline_favorite_24),
                                        contentDescription = "favourite", tint = tint)

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
    }

    if (newsFromSearch.totalResults == 0) {
        Log.d("check", newsFromSearch.totalResults.toString())
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "Nothing found")
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
@Composable
fun SearchTab(
    viewModel: MainViewModel,
) {
    var value by remember {
        mutableStateOf("")
    }
    var query by remember {
        mutableStateOf("")
    }
    var visible by remember {
        mutableStateOf(false)
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
                visible = !visible
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_sort_24),
                    contentDescription = "sort",
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
    AnimatedVisibility(visible = visible) {
        DataPicker()
    }


}

@Composable
fun DataPicker() {
    val mContext = LocalContext.current

    val mYear: Int
    val mMonth: Int
    val mDay: Int
    val mCalendar = Calendar.getInstance()
    mYear = mCalendar.get(Calendar.YEAR)
    mMonth = mCalendar.get(Calendar.MONTH)
    mDay = mCalendar.get(Calendar.DAY_OF_MONTH)
    mCalendar.time = Date()
    val mDate = remember { mutableStateOf("") }
    val mDatePickerDialog = DatePickerDialog(
        mContext,
        { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
            mDate.value = "$mDayOfMonth/${mMonth + 1}/$mYear"
        }, mYear, mMonth, mDay
    )

    Column(modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {

        Button(onClick = {
            mDatePickerDialog.show()
        }, colors = ButtonDefaults.buttonColors(backgroundColor = Color(0XFF0F9D58))) {
            Text(text = "Open Date Picker", color = Color.White)
        }

        Spacer(modifier = Modifier.size(100.dp))
        // Text(text = "Selected Date: ${mDate.value}", fontSize = 30.sp, textAlign = TextAlign.Center)
    }

    var value by remember {
        mutableStateOf("")
    }

    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(10.dp)) {
        OutlinedTextField(
            value = mYear.toString(),
            onValueChange = {
                value = it
            },
            modifier = Modifier.weight(1f),
            label = { Text("From") },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_calendar_month_24),
                    contentDescription = "",
                    tint = MaterialTheme.colors.onSurface
                )
            },
            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_touch_app_24),
                    contentDescription = "",
                    tint = MaterialTheme.colors.onSurface
                )
            },
            readOnly = true
        )
        OutlinedTextField(
            value = mYear.toString(),
            onValueChange = {
                value = it
            },
            modifier = Modifier.weight(1f),
            label = { Text("To") },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_calendar_month_24),
                    contentDescription = "",
                    tint = MaterialTheme.colors.onSurface
                )
            },
            readOnly = true
        )
    }

}

