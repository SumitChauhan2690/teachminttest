package com.example.github_search.data.model.generic


import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.RoomWarnings
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "repo")
data class Item(
    @SerializedName("clone_url")
    val cloneUrl: String,
    @SerializedName("contributors_url")
    val contributorsUrl: String,
    @SerializedName("description")
    val description: String?,
    @SerializedName("forks")
    val forks: Int,
    @SerializedName("full_name")
    val fullName: String,
    @SerializedName("id")
    @PrimaryKey val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("owner")
    @Expose
    @Embedded("owner")
    val owner: Owner,
    @SerializedName("watchers")
    val watchers: Int
)