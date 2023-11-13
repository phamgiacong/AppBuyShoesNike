package com.hn_2452.shoes_nike.data

import java.io.Serializable

data class NetworkResult<T>(
    val success: Boolean,
    val message: String,
    val data: T
) : Serializable