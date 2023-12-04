package com.example.github_search.module.di

import android.content.Context
import android.util.Log
import com.example.github_search.data.repo.GitHubDatabase
import com.example.github_search.data.repo.LocalSource
import com.example.github_search.data.repo.RemoteSource
import com.example.github_search.data.repo.Repository
import com.example.github_search.module.utils.Constant
import com.example.github_search.module.utils.TokenInterceptor
import com.example.github_search.ui.app.GithubSearch
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    /** App Modules */
    @Singleton
    @Provides
    fun provideApplication(): GithubSearch {
        return GithubSearch()
    }

    /** Database Modules */
    @Singleton
    @Provides
    fun provideGitHubDatabase(@ApplicationContext context: Context): GitHubDatabase {
        return GitHubDatabase.getInstance(context)
    }

    /** Network Modules */
    @Singleton
    @Provides
    fun provideTokenIntercepter(): Interceptor {
        return TokenInterceptor()
    }

    @Singleton
    @Provides
    fun provideRetrofit(tokenInterceptor: TokenInterceptor): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constant.BASE_URL)
            .client(
                OkHttpClient().newBuilder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .addInterceptor(tokenInterceptor)
                    .addInterceptor(HttpLoggingInterceptor { message ->
                        Log.d("DI Module", message)
                    }.setLevel(HttpLoggingInterceptor.Level.BODY))
                    .build()
            )
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder().create()
                )
            ).build()
    }

    @Singleton
    @Provides
    fun provideRemoteSource(retrofit: Retrofit): RemoteSource {
        return retrofit.create(RemoteSource::class.java)
    }

    @Singleton
    @Provides
    fun provideLocalSource(githubDatabase: GitHubDatabase): LocalSource {
        return githubDatabase.localSource()
    }


    /** Repository Modules */
    @Singleton
    @Provides
    fun provideRepository(remoteSource: RemoteSource, localSource: LocalSource): Repository {
        return Repository(remoteSource, localSource)
    }
}