package com.hn_2452.shoes_nike.data.model

data class Notification(
    val id: String,
    val title: String,
    val content: String,
    val type: Int,
    val link: String,
    val seen: Boolean
)
