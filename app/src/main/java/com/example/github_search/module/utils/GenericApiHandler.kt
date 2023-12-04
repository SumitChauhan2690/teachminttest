package com.example.github_search.module.utils

import android.util.Log
import com.example.github_search.data.model.generic.GenericApiResponse
import retrofit2.Response

abstract class GenericApiHandler {
    protected suspend fun <T> reqResult(call: () -> Response<GenericApiResponse<T>>) : ApiResource<T>? {
        return try {
            val response = call()
            Log.d(GenericApiHandler::class.java.name, "Response: $response")
            if (response.isSuccessful) {
                val body = response.body()
                ApiResource.success(body, listOf("Data Fetch Successfully"))
            } else {
                ApiResource.error(null, listOf(response.message()))
            }
        } catch (e: Exception) {
            Log.d(GenericApiHandler::class.java.name, "Crash: ${e.printStackTrace()}")
            e.message?.let {
                ApiResource.error(null, listOf(it))
            }
        }
    }
}