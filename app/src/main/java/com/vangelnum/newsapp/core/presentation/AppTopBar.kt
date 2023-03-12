package com.vangelnum.newsapp.core.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.vangelnum.newsapp.R
import kotlinx.coroutines.launch

@Composable
fun AppTopBar(scaffoldState: ScaffoldState) {
    val scope = rememberCoroutineScope()
    val appBarHorizontalPadding = 4.dp
    val titleIconModifier = Modifier
        .fillMaxHeight()
        .width(72.dp - appBarHorizontalPadding)

    TopAppBar(
        backgroundColor = Color.Transparent,
        elevation = 0.dp,
        modifier= Modifier.fillMaxWidth()) {

        Box(Modifier.height(32.dp)) {
            Row(titleIconModifier, verticalAlignment = Alignment.CenterVertically) {
                CompositionLocalProvider(
                    LocalContentAlpha provides ContentAlpha.high,
                ) {
                    IconButton(
                        onClick = {
                                  scope.launch {
                                      scaffoldState.drawerState.open()
                                  }
                        },
                        enabled = true,
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Menu,
                            contentDescription = "Back",
                        )
                    }
                }
            }
            Row(Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically) {

                ProvideTextStyle(value = MaterialTheme.typography.h3) {
                    CompositionLocalProvider(
                        LocalContentAlpha provides ContentAlpha.high,
                    ){
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            maxLines = 1,
                            text = stringResource(id = R.string.news)
                        )
                    }
                }
            }


        }
    }
}