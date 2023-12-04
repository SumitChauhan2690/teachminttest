package com.example.github_search.module.utils

import com.example.github_search.data.model.generic.GenericApiResponse

data class ApiResource<out T>(val status: Status, val data: GenericApiResponse<@UnsafeVariance T>?, val message: String?) {

    enum class Status {
        SUCCESS,
        ERROR,
        LOADING
    }

    companion object {
        fun <T> success(data: GenericApiResponse<T>? = null, message: List<String>) : ApiResource<T> {
            return ApiResource(Status.SUCCESS, data, message.firstOrNull().toString())
        }

        fun <T> error(data: GenericApiResponse<T>? = null, message: List<String>) : ApiResource<T> {
            return ApiResource(Status.ERROR, data, message.firstOrNull().toString())
        }

        fun <T> loading(data: GenericApiResponse<T>? = null) : ApiResource<T> {
            return ApiResource(Status.LOADING, data, "loading...")
        }
    }
}