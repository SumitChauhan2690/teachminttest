package com.example.github_search.data.model.generic


import com.google.gson.annotations.SerializedName

data class GitRepoSearch(
    @SerializedName("incomplete_results")
    val incompleteResults: Boolean,
    @SerializedName("items")
    val items: List<Item>,
    @SerializedName("total_count")
    val totalCount: Int
)