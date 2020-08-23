package com.ann.m17test.data.model

/**
 * RepoSearchResult from a search, which contains List<Repo> holding query data,
 * and a String of network error state.
 */
sealed class UserSearchResult {
    data class Success(val data: List<User>) : UserSearchResult()
    data class Error(val error: Exception) : UserSearchResult()
}
