package com.vangelnum.newsapp

import android.app.DatePickerDialog
import android.content.Context
import android.widget.DatePicker
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.vangelnum.newsapp.feature_search.presentation.SearchViewModel
import java.util.Calendar
import java.util.Date


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FilterContent(
    query: String,
    searchViewModel: SearchViewModel,
    mContext: Context
) {
    val currentYear: Int
    val currentMonth: Int
    val currentDay: Int
    val calendar = Calendar.getInstance()
    currentYear = calendar.get(Calendar.YEAR)
    currentMonth = calendar.get(Calendar.MONTH)
    currentDay = calendar.get(Calendar.DAY_OF_MONTH)
    calendar.time = Date()
    val date1 = rememberSaveable { mutableStateOf("") }
    val date2 = rememberSaveable { mutableStateOf("") }

    val datePickerDialog1 =
        DatePickerDialog(
            mContext, { _: DatePicker, year1: Int, month1: Int, dayOfMonth1: Int ->
                date1.value = "$year1-${month1 + 1}-$dayOfMonth1"
            }, currentYear, currentMonth, currentDay
        )

    val datePickerDialog2 =
        DatePickerDialog(
            mContext, { _: DatePicker, year2: Int, month2: Int, dayOfMonth2: Int ->
                date2.value = "$year2-${month2 + 1}-$dayOfMonth2"
            }, currentYear, currentMonth, currentDay
        )

    val listItems = listOf(
        "publishedAt",
        "popularity",
        "relevancy",
    )

    var expanded by remember {
        mutableStateOf(false)
    }

    var selectedItem by remember {
        mutableStateOf(listItems[0])
    }
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(start = 16.dp, end = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = date1.value,
                onValueChange = {

                },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clickable {
                        datePickerDialog1.show()
                    },
                label = { Text(stringResource(id = R.string.from)) },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_calendar_month_24),
                        contentDescription = null,
                    )
                },
                readOnly = true,
                enabled = false,
                maxLines = 1,
            )
            OutlinedTextField(
                value = date2.value,
                onValueChange = {

                },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clickable {
                        datePickerDialog2.show()
                    },
                label = { Text(stringResource(id = R.string.to)) },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_calendar_month_24),
                        contentDescription = null,
                    )
                },
                readOnly = true,
                enabled = false,
                maxLines = 1,
            )
        }
        ExposedDropdownMenuBox(
            modifier = Modifier.fillMaxWidth(),
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            }
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = selectedItem,
                onValueChange = {},
                readOnly = true,
                label = { Text(text = stringResource(id = R.string.sorted_by)) },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = expanded
                    )
                },
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                listItems.forEach { selectedOption ->
                    DropdownMenuItem(onClick = {
                        selectedItem = selectedOption
                        expanded = false
                    }) {
                        Text(text = selectedOption)
                    }
                }
            }
        }
        OutlinedButton(
            onClick = {
                searchViewModel.getSearchNews(
                    query = query,
                    sortBy = selectedItem,
                    from = date1.value,
                    to = date2.value
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
        ) {
            Text(text = stringResource(id = R.string.search))
        }
        Spacer(modifier = Modifier.height(16.dp))

    }

}