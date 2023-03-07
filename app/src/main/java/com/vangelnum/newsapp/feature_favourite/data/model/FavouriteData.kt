package com.vangelnum.newsapp.feature_favourite.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "room_table")
data class FavouriteData(
    @PrimaryKey(autoGenerate = false)
    val urlPhoto: String,
    val content: String,
    val time: String,
)