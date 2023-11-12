package com.hn_2452.shoes_nike.utility

data class Result<T>(
    val success: Boolean,
    val data: T?,
    val exception: Exception?,
    var loading: Boolean = false
)