package com.vangelnum.newsapp

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.vangelnum.newsapp.core.presentation.Screens
import com.vangelnum.newsapp.feature_search.presentation.SearchViewModel
import java.util.Calendar
import java.util.Date


@Composable
fun FilterScreen(
    viewModel: SearchViewModel = hiltViewModel(),
    query: String?,
    sortBy: String?,
    navController: NavController,
) {

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
    val mDate2 = remember { mutableStateOf("") }

    val mDatePickerDialog =
        DatePickerDialog(
            mContext, { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
                mDate.value = "$mYear-${mMonth + 1}-$mDayOfMonth"
            }, mYear, mMonth, mDay
        )

    val mDatePickerDialog2 =
        DatePickerDialog(
            mContext, { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
                mDate2.value = "$mYear-${mMonth + 1}-$mDayOfMonth"
            }, mYear, mMonth, mDay
        )

    var visible by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(key1 = mDate.value) {
        if (mDate.value != "") {
            visible = true
        }
    }
    AnimatedVisibility(visible = visible, enter = fadeIn(), exit = fadeOut()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            OutlinedButton(
                onClick = {
                    viewModel.getSearchNews(
                        query = query!!,
                        sortBy = sortBy!!,
                        mDate.value,
                        mDate2.value
                    )
                    navController.navigate(Screens.SearchScreen.route)
                },
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White,
                    backgroundColor = Color.Black
                ), border = BorderStroke(1.dp, Color.DarkGray),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
            ) {
                Text(text = "Применить")
            }
        }
    }

    Row {

    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp, bottom = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = mDate.value,
            onValueChange = {

            },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clickable {
                    mDatePickerDialog.show()
                },
            label = { Text("From") },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_calendar_month_24),
                    contentDescription = "",
                    tint = MaterialTheme.colors.onSurface
                )
            },
            readOnly = true,
            enabled = false,
            maxLines = 1,
            colors = TextFieldDefaults.textFieldColors(
                disabledTextColor = Color.White
            )
        )
        OutlinedTextField(
            value = mDate2.value,
            onValueChange = {

            },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clickable {
                    mDatePickerDialog2.show()
                },
            label = { Text("To") },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_calendar_month_24),
                    contentDescription = "",
                    tint = MaterialTheme.colors.onSurface
                )
            },
            readOnly = true,
            enabled = false,
            maxLines = 1,
            colors = TextFieldDefaults.textFieldColors(
                disabledTextColor = Color.White
            )
        )
    }
}