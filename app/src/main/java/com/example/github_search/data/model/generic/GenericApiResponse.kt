package com.example.github_search.data.model.generic


import com.google.gson.annotations.SerializedName

data class GenericApiResponse<T>(
    @SerializedName("incomplete_results")
    val incompleteResults: Boolean,
    @SerializedName("items")
    val items: T,
    @SerializedName("total_count")
    val totalCount: Int
)