package com.example.github_search.module.utils

sealed class Resource<T>(
    val message: String? = null,
    val data: T? = null,
    val status: Status
) {
    enum class Status {
        SUCCESS,
        ERROR,
        LOADING
    }

    class Success<T>(message: String, data: T?) : Resource<T>(message, data, Status.SUCCESS)
    class Error<T>(message: String, data: T? = null) : Resource<T>(message, data, Status.ERROR)
    class Loading<T> : Resource<T>(status = Status.LOADING)
}