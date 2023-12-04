package com.example.github_search.data.repo

import com.example.github_search.data.model.generic.ContributorItem
import com.example.github_search.data.model.generic.GenericApiResponse
import com.example.github_search.data.model.generic.Item
import retrofit2.Response
import retrofit2.http.HTTP
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface RemoteSource {

    @HTTP(method = "GET", path = "/search/repositories", hasBody = false)
    suspend fun repoSearchReq(
        @Query("q") searchKeyWord: String?,
        @Query("page") page: String,
        @Query("per_page") per_page: String
    ): Response<GenericApiResponse<List<Item>>>

    @HTTP(method = "GET", path = "/users/{name}/repos", hasBody = false)
    suspend fun contributorRepoReq(
        @Path("name") searchKeyWord: String?,
        @Query("page") page: String,
        @Query("per_page") per_page: String
    ): Response<List<Item>>

    @HTTP(method = "GET", path = "/repos/{login}/{name}/contributors", hasBody = false)
    suspend fun contributorListReq(
        @Path("login") login: String,
        @Path("name") name: String
    ): Response<List<ContributorItem>>
}