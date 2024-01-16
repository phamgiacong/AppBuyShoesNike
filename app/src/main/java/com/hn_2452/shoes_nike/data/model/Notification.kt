package com.hn_2452.shoes_nike.data.model

data class Notification(
    val id_user:String?,
    val _id: String,
    val title: String,
    val content: String,
    val type: String,
    val link: String,
    val seen: Boolean,
    val time:Long
)
