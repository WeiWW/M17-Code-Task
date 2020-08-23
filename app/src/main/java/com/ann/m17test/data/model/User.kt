package com.ann.m17test.data.model

data class GithubUser(
    val incomplete_results: Boolean,
    val items: List<User>,
    val total_count: Int
)

data class User(
    val id: Int = 0,
    val login: String = "",
    val avatar_url: String = ""
)