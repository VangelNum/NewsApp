package com.vangelnum.newsapp.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.vangelnum.newsapp.R

val UbuntyFonts = FontFamily(
    Font(R.font.ubuntylight),
    Font(R.font.ubuntubold, FontWeight.Bold),
    Font(R.font.ubuntyregular, FontWeight.Normal),
    Font(R.font.ubuntumedium, FontWeight.Medium),
    Font(R.font.ubuntuitalic, FontWeight.Normal, FontStyle.Italic),
    Font(R.font.ubuntumediumitalic, FontWeight.Medium, FontStyle.Italic),
    Font(R.font.ubuntubolditalic, FontWeight.Bold, FontStyle.Italic),
)

val Typography = Typography(
    body1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    h1 = TextStyle(
        fontFamily = UbuntyFonts,
        fontWeight = FontWeight.Bold,
        fontSize = 30.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.sp
    ),
    h2 = TextStyle(
        fontFamily = UbuntyFonts,
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp,
    ),
    h3 = TextStyle(
        fontFamily = UbuntyFonts,
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium,
        letterSpacing = 0.sp
    ),
    h4 = TextStyle(
        fontFamily = UbuntyFonts,
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal,
        letterSpacing = 0.sp
    )

    /* Other default text styles to override
    button = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
    */
)