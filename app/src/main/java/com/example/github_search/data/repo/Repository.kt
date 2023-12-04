package com.example.github_search.data.repo

import android.annotation.SuppressLint
import android.util.Log
import androidx.room.TypeConverter
import com.example.github_search.data.model.generic.GenericApiResponse
import com.example.github_search.data.model.generic.Item
import com.example.github_search.module.utils.GenericApiHandler
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(
    private var remoteSource: RemoteSource,
    private var localSource: LocalSource
) : GenericApiHandler() {

    suspend fun repoSearchReq(
        searchKeyWord: String,
        page: String,
        per_page: String
    ): Response<GenericApiResponse<List<Item>>> {
        val repositoryList = remoteSource.repoSearchReq(searchKeyWord, page, per_page)
        fromRepositoryList(repositoryList.body()?.items)?.let {
            Log.d(
                Repository::class.java.name,
                "Repository Json: $it"
            )
        }

        repositoryList.body()?.let { localSource.insert(it.items) }
        return repositoryList
    }

    @SuppressLint("SuspiciousIndentation")
    fun getRepositoryFromLocal(searchKeyWord: String): List<Item> {
        var itemList: List<Item> = emptyList()
        itemList = localSource.getRepository(searchKeyWord)
        Log.d(
            Repository::class.java.name,
            "Repository From DB$$$: $itemList"
        )
        return itemList
    }

    suspend fun contribursRepoReq(name: String, page: String, per_page: String) =
        remoteSource.contributorRepoReq(name, page, per_page)

    suspend fun contributorListReq(login: String, name: String) =
        remoteSource.contributorListReq(login, name)

    @TypeConverter
    fun fromRepositoryList(countryLang: List<Item?>?): String? {
        val type = object : TypeToken<List<Item?>?>() {}.type
        return Gson().toJson(countryLang, type)
    }

    @TypeConverter
    fun toRepositoryList(countryLangString: String?): List<Item>? {
        val type = object : TypeToken<List<Item?>?>() {}.type
        return Gson().fromJson<List<Item>>(countryLangString, type)
    }
}