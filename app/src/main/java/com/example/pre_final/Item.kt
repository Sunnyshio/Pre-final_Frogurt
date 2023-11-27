package com.example.pre_final

import java.io.Serializable

data class Item(
    val id: String,
    val name: String?,
    val description: String?,
    val price: String?,
    val url: String?
) : Serializable
