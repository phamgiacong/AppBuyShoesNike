package com.hn_2452.shoes_nike.data.model

data class Shoes(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val price: Long = -1,
    val shoesType: String = "",
    val rate: Float = -1F,
    val sold: Long = -1L,
    val availableColor: List<String> = emptyList(),
    val availableSize: List<Int> = emptyList(),
    val mainImage : String = "",
    val images: List<String> = emptyList(),
    val gender: Int = -1,
    val createdDate: Long = -1L,
    val state: Int = -1
)