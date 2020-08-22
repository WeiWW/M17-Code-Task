package com.ann.m17test.data.model

import com.squareup.moshi.Json

data class User(
    @Json(name = "id")
    val id: Int = 0,
    @Json(name = "login")
    val name: String = "",
    @Json(name = "avatar_url")
    val avatar: String = ""
)