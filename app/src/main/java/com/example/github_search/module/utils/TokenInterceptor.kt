package com.example.github_search.module.utils

import android.util.Log
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val networkErrorMessage = "{\n" +
                "    \"success\": false,\n" +
                "    \"message\": [\n" +
                "        \"No Network Connection\"\n" +
                "    ],\n" +
                "    \"data\": {}\n" +
                "}"

        return try {
            Log.d(TokenInterceptor::class.java.name, "Yes Network")
            chain.proceed(
                chain.request().newBuilder()
                    .header(
                        "Authorization",
                        ""
                    ).build()
            )
        } catch (e: Exception) {
            e.printStackTrace()
            Response.Builder().request(chain.request()).protocol(Protocol.HTTP_1_1).message("No Network Connection").body(networkErrorMessage.toResponseBody(
                "application/json; charset=utf-8".toMediaType()
            )).code(504).build()
        }
    }
}