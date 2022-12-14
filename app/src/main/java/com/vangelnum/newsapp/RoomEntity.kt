package com.vangelnum.newsapp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "room_table")
data class RoomEntity(
    @PrimaryKey(autoGenerate = false)
    val urlPhoto: String,
    val content: String,
    val time: String,
)