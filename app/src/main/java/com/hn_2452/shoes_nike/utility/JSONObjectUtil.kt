package com.hn_2452.shoes_nike.utility

import org.json.JSONObject

@Suppress("UNCHECKED_CAST")
fun <T> JSONObject.getOrNull(key: String) : T? {
    if(has(key)) {
        return get(key) as T
    }
    return null
}