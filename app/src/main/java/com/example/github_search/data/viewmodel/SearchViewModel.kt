package com.example.github_search.data.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.github_search.R
import com.example.github_search.data.model.generic.GenericApiResponse
import com.example.github_search.data.model.generic.Item
import com.example.github_search.data.repo.Repository
import com.example.github_search.module.utils.Resource
import com.example.github_search.ui.app.GithubSearch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: Repository,
    private val githubsearch: Application
) : AndroidViewModel(githubsearch) {
    var searchedRepo = MutableLiveData<Resource<GenericApiResponse<List<Item>>>>()

    fun getSearchedRepository(searchKeyWord: String, page: String, per_page: String) {
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                if (githubsearch.applicationContext.hasInternetConnection()) {
                    searchedRepo.postValue(Resource.Loading())
                    searchedRepo.postValue(
                        handleApiResponse(
                            repository.repoSearchReq(
                                searchKeyWord,
                                page,
                                per_page
                            )
                        )
                    )
                } else {
                    searchedRepo.postValue(
                        Resource.Error(
                            message = getApplication<GithubSearch>().resources.getString(
                                R.string.no_internet
                            )
                        )
                    )

                    searchedRepo.postValue(Resource.Loading())
                    searchedRepo.postValue(
                        handleLocalStorageResponse(
                            repository.getRepositoryFromLocal(searchKeyWord)
                        )
                    )

                    Log.d(SearchViewModel::class.java.name,
                        "Repository From DB#####: ${repository.getRepositoryFromLocal(searchKeyWord)}"
                    )
                }
            }
        }
    }

    private fun <T> handleApiResponse(response: Response<GenericApiResponse<T>>?): Resource<GenericApiResponse<T>> {
        if (response?.isSuccessful!!) {
            response.body()?.let { res ->
                return Resource.Success("Data Fetch Successfully", res)
            }
        }
        return Resource.Error(response.message())
    }

    private fun <T> handleLocalStorageResponse(response: T): Resource<GenericApiResponse<T>> {
        return Resource.Success("Data Fetch Successfully", GenericApiResponse(false, response, 0))
    }

    private fun Context.hasInternetConnection(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
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