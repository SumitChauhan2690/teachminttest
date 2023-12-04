package com.example.github_search.data.repo

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.github_search.data.model.generic.Demo
import com.example.github_search.data.model.generic.Item
import com.example.github_search.data.model.generic.License
import com.example.github_search.data.model.generic.Owner
import com.example.github_search.module.utils.Constant.DATABASE_NAME

@Database(entities = [Demo::class, Item::class, Owner::class], version = 1, exportSchema = false)
abstract class GitHubDatabase  : RoomDatabase() {

    abstract fun localSource() : LocalSource

    companion object {
        @Volatile private var INSTANCE: GitHubDatabase? = null

        fun getInstance(context: Context): GitHubDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(context, GitHubDatabase::class.java, DATABASE_NAME).build()
                INSTANCE = instance
                instance
            }
        }
    }
}