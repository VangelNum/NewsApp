package com.vangelnum.newsapp

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.vangelnum.newsapp.ui.theme.Purple200
import com.vangelnum.newsapp.ui.theme.Purple700

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuggestionChipEachRow(
    chip: String,
    selected: Boolean,
    onChipState: (String) -> Unit,
) {

    SuggestionChip(onClick = {
        if (!selected)
            onChipState(chip)
        else
            onChipState("")
    }, label = {
        Text(text = chip)
    },
        border = SuggestionChipDefaults.suggestionChipBorder(
            borderWidth = 1.dp,
            borderColor = if (selected) Color.Transparent else Purple200
        ),
        modifier = Modifier.padding(horizontal = 5.dp),
        colors = SuggestionChipDefaults.suggestionChipColors(
            containerColor = if (selected) Purple700 else Color.Transparent
        ),
        shape = RoundedCornerShape(16.dp)
    )

}