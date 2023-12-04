package com.example.github_search.data.model.generic

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "demo")
data class Demo(
    @PrimaryKey val id: Int,
    val name: String
)
