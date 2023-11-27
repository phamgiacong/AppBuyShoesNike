package com.hn_2452.shoes_nike.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Searching(
    @PrimaryKey(autoGenerate = false)
    val content: String
)