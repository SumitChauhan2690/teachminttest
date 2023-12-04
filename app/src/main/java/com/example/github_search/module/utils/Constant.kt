package com.example.github_search.module.utils

import javax.inject.Singleton

@Singleton
object Constant {
    // Network constant
    const val BASE_URL = "https://api.github.com"

    // Database constant
    const val DATABASE_NAME = "db-github-search"
    const val STORAGE_REQUEST_CODE = 100
}