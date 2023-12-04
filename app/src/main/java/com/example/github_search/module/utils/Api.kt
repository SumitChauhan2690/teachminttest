package com.example.github_search.module.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.github_search.data.model.generic.GenericApiResponse
import retrofit2.Response
import javax.inject.Singleton

@Singleton
object Api {
    fun <T> handleApiResponse(response: Response<GenericApiResponse<T>>?): Resource<GenericApiResponse<T>> {
        if (response?.isSuccessful!!) {
            response.let { res ->
                return if (res.isSuccessful) {
                    res.let {
                        Resource.Success("Data Fetched", it.body())
                    }
                } else {
                    Resource.Error("Data Fetched Error", res.body())
                }
            }
        }
        return Resource.Error(response.message())
    }

    fun hasInternetConnection(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}