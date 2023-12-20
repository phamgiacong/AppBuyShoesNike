package com.hn_2452.shoes_nike.data.network_result

import com.hn_2452.shoes_nike.data.model.User

data class LoginResult(
    val token: String,
    val user: User
)