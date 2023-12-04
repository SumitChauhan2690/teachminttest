package com.example.github_search.data.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.github_search.R
import com.example.github_search.data.model.generic.ContributorItem
import com.example.github_search.data.model.generic.GenericApiResponse
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
class DetailsViewModel @Inject constructor(
    private val repository: Repository,
    private val githubsearch: Application
) : AndroidViewModel(githubsearch) {
    var contributor = MutableLiveData<Resource<List<ContributorItem>>>()

    fun getContributor(login: String, name: String) {
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                if (githubsearch.applicationContext.hasInternetConnection()) {
                    contributor.postValue(Resource.Loading())
                    contributor.postValue(
                        handleApiResponse(
                            repository.contributorListReq(
                                login, name
                            )
                        )
                    )
                } else {
                    contributor.postValue(
                        Resource.Error(
                            message = getApplication<GithubSearch>().resources.getString(
                                R.string.no_internet
                            )
                        )
                    )
                }
            }
        }
    }

    private fun <T> handleApiResponse(response: Response<T>?): Resource<T> {
        if (response?.isSuccessful!!) {
            response.body()?.let { res ->
                return Resource.Success("Data Fetch Successfully", res)
            }
        }
        return Resource.Error(response.message())
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