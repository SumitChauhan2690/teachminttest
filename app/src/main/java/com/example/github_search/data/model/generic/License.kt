package com.example.github_search.data.model.generic


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable


@Entity(tableName = "license")
data class License(
    @SerializedName("key")
    val key: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("node_id")
    val nodeId: String,
    @SerializedName("spdx_id")
    val spdxId: String,
    @SerializedName("url")
    val url: String
) :Serializable