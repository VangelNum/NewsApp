package com.vangelnum.newsapp.core.presentation.drawer_layout

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.DrawerState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ListItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.navOptions
import com.vangelnum.newsapp.R
import com.vangelnum.newsapp.core.presentation.Screens
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun DrawerHeader() {
    Box(
        modifier = Modifier
            .width(100.dp)
            .height(100.dp)
            .padding(16.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colors.primaryVariant),
        contentAlignment = Alignment.Center,
        content = {
            Icon(
                Icons.Filled.Person,
                contentDescription = "Person icon",
                modifier = Modifier.size(40.dp)
            )
        }
    )
    Text(
        text = stringResource(id = R.string.news_vangelnum),
        style = MaterialTheme.typography.h3,
        modifier = Modifier.padding(start = 16.dp, bottom = 16.dp)
    )
    Divider()

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DrawerBody(navController: NavController, drawerState: DrawerState) {

    val items = listOf(
        DrawerItems.Contacts,
        DrawerItems.Share,
        DrawerItems.SoundBoard,
        DrawerItems.DrumPad,
        DrawerItems.Wallpaper,
        DrawerItems.Pizza,
        DrawerItems.UnsplashPhotos
    )
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    LazyColumn() {
        itemsIndexed(items) { index, item ->
            ListItem(
                modifier = Modifier.clickable {
                    onEvent(item, navController, context, scope, drawerState)
                },
                icon = {
                    if (index > 1) {
                        Image(
                            painter = painterResource(id = item.icon),
                            contentDescription = item.title,
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(
                            painter = painterResource(id = item.icon),
                            contentDescription = item.title,
                            modifier = Modifier.size(24.dp),
                        )
                    }
                },
                text = {
                    Text(text = item.title)
                },
            )
        }
    }
}


fun onEvent(
    title: DrawerItems,
    navController: NavController,
    context: Context,
    scope: CoroutineScope,
    drawerState: DrawerState
) {

    when (title) {
        is DrawerItems.Share -> {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TITLE, "Спасибо за то, что поделился приложением! ❤")
                putExtra(
                    Intent.EXTRA_TEXT,
                    "https://github.com/VangelNum/NewsApp"
                )
                type = "text/plain"
            }
            context.startActivity(Intent.createChooser(sendIntent, "Share..."))
        }

        is DrawerItems.SoundBoard -> {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data =
                Uri.parse("https://play.google.com/store/apps/details?id=com.zxcursedsoundboard.apk")
            context.startActivity(intent)
        }

        is DrawerItems.DrumPad -> {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data =
                Uri.parse("https://play.google.com/store/apps/details?id=com.vangelnum.drumpad")
            context.startActivity(intent)
        }

        is DrawerItems.Contacts -> {
            scope.launch {
                drawerState.close()
            }
            navController.navigate(Screens.ContactScreen.route, navOptions {
                launchSingleTop = true
            })
        }

        is DrawerItems.Pizza -> {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data =
                Uri.parse("https://play.google.com/store/apps/details?id=com.vangelnum.pizza")
            context.startActivity(intent)
        }

        is DrawerItems.Wallpaper -> {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data =
                Uri.parse("https://play.google.com/store/apps/details?id=com.zxcursed.wallpaper")
            context.startActivity(intent)
        }

        is DrawerItems.UnsplashPhotos -> {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data =
                Uri.parse("https://github.com/VangelNum/Unsplash_Photos")
            context.startActivity(intent)
        }
    }
}
